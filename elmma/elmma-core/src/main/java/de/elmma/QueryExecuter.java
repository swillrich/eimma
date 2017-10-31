package de.elmma;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import de.elmma.ElmmaHibernateConfiguration.HBM2DDL_AUTO;

class QueryExecuter {

	public static <T> T take(String sql, OnQuery<?> qe) {
		SessionFactory factory = new ElmmaHibernateConfiguration(HBM2DDL_AUTO.UPDATE).buildSessionFactory();
		Session session = factory.openSession();
		Query query = session.createQuery(sql);
		Object e = qe.work(query);
		session.close();
		factory.close();
		return (T) e;
	}

	interface OnQuery<T> {
		T work(Query query);
	}
}