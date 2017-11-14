package de.elmma.dbio;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import lombok.Data;

/**
 * Liefert eine vorkonfigurierte Hibernate-Session und vereinfacht das Reinschreiben/Rausschreiben in die DB.
 *
 */
public class SessionProvider {

	/**
	 * Nimmt ein Hibernate-SQL und implementiere, was geschehen soll
	 */
	public static <T> T take(String sql, OnQuery<?> qe) {
		return (T) new HibernateSessionProvider() {

			@Override
			public Object work(Session session) {
				Query query = session.createQuery(sql);
				return qe.work(query);
			}
		}.getResult();
	}

	public static Object save(OnSave onSave) {
		return new HibernateSessionProvider() {

			@Override
			public Object work(Session session) {
				Transaction transaction = session.beginTransaction();
				Object o = onSave.work(session);
				transaction.commit();
				return o;
			}
		}.getResult();
	}

	public static interface OnSave {
		Object work(Session session);
	}

	public static interface OnQuery<T> {
		T work(Query query);
	}

	@Data
	public static abstract class HibernateSessionProvider {
		private Object result;

		public abstract Object work(Session session);

		public HibernateSessionProvider() {
			SessionFactory factory = ElmmaHibernateConfiguration.getInstance().buildSessionFactory();
			Session session = factory.openSession();
			this.result = work(session);
			session.close();
		}
	}
}