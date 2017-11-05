package de.elmma.services;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import de.elmma.controller.JSONURLReader;
import de.elmma.dbio.SessionProvider;
import de.elmma.model.Price;

@Service("CurrentPriceFetcher")
public class IntradayFetcher {

	SimpleDateFormat format = new SimpleDateFormat("dd. MMM yyyy HH:mm");

	public IntradayFetcher() {
		Price lastPrice = SessionProvider.take("FROM Price p ORDER BY p.datetime DESC", q -> {
			q.setMaxResults(1);
			return q.uniqueResult();
		});
		System.out.println("####### take the last price: " + lastPrice);
		while (true) {
			try {
				Price price = fetchCurrentPrice();
				if (price.getDatetime().getTime() > lastPrice.getDatetime().getTime()) {
					SessionProvider.save(session -> session.save(price));
					lastPrice = price;
					System.out.println("####### saved new price: " + lastPrice);
				}
				Thread.sleep(1000);
			} catch (ParseException | InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Price fetchCurrentPrice() throws IOException, ParseException {
		JSONObject json = JSONURLReader.readJsonFromUrl("http://elmma-exchange-api:8080/snapshot");
		NumberFormat nrformat = NumberFormat.getNumberInstance(Locale.GERMANY);
		double price = nrformat.parse(json.getString("price")).doubleValue();
		String dateAsString = json.getString("date") + " " + json.getString("time");
		Date date = format.parse(dateAsString);
		return new Price("DAX", date, price);
	}
}
