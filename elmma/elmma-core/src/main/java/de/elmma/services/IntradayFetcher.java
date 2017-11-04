package de.elmma.services;

//@Service("historicalDateFetcher")
//unused
public class IntradayFetcher {
	public IntradayFetcher() {
//		while (true) {
//
//			JSONObject json = JSONURLReader.readJsonFromUrl("http://elmma-exchange-api:8080/snapshot");
//			JSONObject price = json.getJSONObject("price");
//			JSONObject date = json.getJSONObject("date");
//			JSONObject time = json.getJSONObject("time");
//
//			List<Price> prices = new ArrayList<Price>();
//
//			for (int i = 0; dates.has(String.valueOf(i)); i++) {
//				String id = String.valueOf(i);
//				String date = (String) dates.get(id);
//				double open = (double) opens.get(id);
//
//				String[] ds = date.split("-");
//
//				Price price = new Price(underlying,
//						new Date(Integer.valueOf(ds[0]), Integer.valueOf(ds[1]), Integer.valueOf(ds[2])), open);
//				prices.add(price);
//			}
//
//			ElmmaHibernateConfiguration configuration = new ElmmaHibernateConfiguration(HBM2DDL_AUTO.UPDATE);
//			SessionFactory factory = configuration.buildSessionFactory();
//			Session session = factory.openSession();
//			Transaction transaction = session.beginTransaction();
//			prices.forEach(p -> session.save(p));
//			transaction.commit();
//			session.close();
//			System.out.println("finished");
//		}
	}
}
