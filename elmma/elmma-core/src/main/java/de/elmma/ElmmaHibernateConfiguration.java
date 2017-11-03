package de.elmma;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import de.elmma.model.Price;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class ElmmaHibernateConfiguration extends Configuration {

	public static enum HBM2DDL_AUTO {
		CREATE, UPDATE;
	}

	public ElmmaHibernateConfiguration(HBM2DDL_AUTO value) {
		configure();
		setProperty("hibernate.hbm2ddl.auto", value.toString().toLowerCase());
	}

	@Override
	public SessionFactory buildSessionFactory() {
		ArrayList<Class> classes = new ArrayList<Class>();

		// the following will detect all classes that are annotated as @Entity
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

		for (BeanDefinition bd : scanner.findCandidateComponents(Price.class.getPackage().toString())) {
			String name = bd.getBeanClassName();
			try {
				classes.add(Class.forName(name));
				System.out.println("------------>" + name);
			} catch (Exception E) {
				// TODO: handle exception - couldn't load class in question
			}
		}
		String packageName = "de.elmma.model";
		List<String> classNames = new FastClasspathScanner(packageName).scan().getNamesOfAllClasses();
		String packageNamePrefix = packageName + ".";
		for (String className : classNames) {
			if (className.startsWith(packageNamePrefix)) {
				try {
					Class<?> clazz = Class.forName(className);
					addAnnotatedClass(clazz);
					System.out.println("Map class: " + clazz.toString());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		return super.buildSessionFactory();
	}
}
