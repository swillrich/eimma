package de.elmma.dbio;

import java.util.List;

import javax.persistence.Entity;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import de.elmma.model.Price;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class ElmmaHibernateConfiguration extends Configuration {
	
	private static ElmmaHibernateConfiguration instance;
	private SessionFactory sessionFactory = null;
	
	public static ElmmaHibernateConfiguration getInstance() {
		if (instance == null) {
			instance = new ElmmaHibernateConfiguration(HBM2DDL_AUTO.UPDATE);
		}
		return instance;
	}

	public static enum HBM2DDL_AUTO {
		CREATE, UPDATE;
	}

	private ElmmaHibernateConfiguration(HBM2DDL_AUTO value) {
		configure();
		setProperty("hibernate.hbm2ddl.auto", value.toString().toLowerCase());
	}

	@Override
	public SessionFactory buildSessionFactory() {
		if (sessionFactory != null) {
			return sessionFactory;
		}
		String packageName = Price.class.getPackage().getName();
		List<String> classNames = new FastClasspathScanner(packageName).scan().getNamesOfAllClasses();
		String packageNamePrefix = packageName + ".";
		for (String className : classNames) {
			if (className.startsWith(packageNamePrefix)) {
				try {
					Class<?> clazz = Class.forName(className);
					if (clazz.isAnnotationPresent(Entity.class)) {
						addAnnotatedClass(clazz);
					}
					System.out.println("Map class: " + clazz.toString());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		sessionFactory = super.buildSessionFactory();
		return sessionFactory;
	}
}
