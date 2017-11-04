package de.elmma.dbio;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import de.elmma.dbio.ElmmaHibernateConfiguration.HBM2DDL_AUTO;
import lombok.Data;

public class SessionProvider {

	public static <T> T take(String sql, OnQuery<?> qe) {
		return (T) new HibernateSessionProvider() {

			@Override
			Object work(Session session) {
				Query query = session.createQuery(sql);
				return qe.work(query);
			}
		}.getResult();
	}

	public static Object save(OnSave onSave) {
		return new HibernateSessionProvider() {

			@Override
			Object work(Session session) {
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
	private static abstract class HibernateSessionProvider {
		private Object result;

		abstract Object work(Session session);

		public HibernateSessionProvider() {
			SessionFactory factory = new ElmmaHibernateConfiguration(HBM2DDL_AUTO.UPDATE).buildSessionFactory();
			Session session = factory.openSession();
			this.result = work(session);
			session.close();
			factory.close();
		}
	}
}