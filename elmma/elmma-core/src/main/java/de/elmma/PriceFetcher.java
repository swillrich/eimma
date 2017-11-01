package de.elmma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import de.elmma.ElmmaHibernateConfiguration.HBM2DDL_AUTO;
import de.elmma.model.Price;

@Service("pricefetcher")
public class PriceFetcher {
	public PriceFetcher() {
		JSONObject json;
		try {
			String underlying = "^GDAXI";
			json = readJsonFromUrl(
					"http://elmma-exchange-api:8080/history/?ticker=" + underlying + "&from=2016-01-01&to=2017-01-01");
			JSONObject dates = json.getJSONObject("Date");
			JSONObject opens = json.getJSONObject("Open");

			List<Price> prices = new ArrayList<Price>();

			for (int i = 0; dates.has(String.valueOf(i)); i++) {
				String id = String.valueOf(i);
				String date = (String) dates.get(id);
				double open = (double) opens.get(id);

				String[] ds = date.split("-");

				Price price = new Price(underlying,
						new Date(Integer.valueOf(ds[0]), Integer.valueOf(ds[1]), Integer.valueOf(ds[2])), open);
				prices.add(price);
			}
			
			ElmmaHibernateConfiguration configuration = new ElmmaHibernateConfiguration(HBM2DDL_AUTO.UPDATE);
			SessionFactory factory = configuration.buildSessionFactory();
			Session session = factory.openSession();
			Transaction transaction = session.beginTransaction();
			prices.forEach(p -> session.save(p));
			transaction.commit();
			session.close();
			System.out.println("finished");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}
}
