package de.elmma.services;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.elmma.controller.JSONURLReader;
import de.elmma.dbio.SessionProvider;
import de.elmma.model.Price;

@Component
/**
 * Dieser Service ist nur dafür da, um Daten aus elmma-exchange-api bei
 * Kursänderung in die Datenbank zu schreiben, weil wir diese Werte für spätere
 * Analysezwecke widerverwenden wollen.
 * 
 * @author Danilo.Schmidt
 *
 */
public class IntradayFetcher {

	private static final Logger log = LoggerFactory.getLogger(IntradayFetcher.class);

	SimpleDateFormat format = new SimpleDateFormat("dd. MMM yyyy HH:mm");

	Price currentPrice = null;

	public IntradayFetcher() {
		currentPrice = SessionProvider.take("FROM Price p ORDER BY p.datetime DESC", q -> {
			q.setMaxResults(1);
			return q.uniqueResult();
		});
		log.info("####### take the last price: " + currentPrice);
	}

	@Scheduled(fixedRate = 1000)
	private void requestNewPrice() {
		try {
			Price price = fetchCurrentPrice();
			if (price.getPrice() != currentPrice.getPrice()) {
				SessionProvider.save(session -> session.save(price));
				currentPrice = price;
				log.info("####### saved new price: " + currentPrice);
			}
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}

	private Price fetchCurrentPrice() throws IOException, ParseException {
		JSONObject json = JSONURLReader.readJsonFromUrl("http://elmma-exchange-api:8080");
		NumberFormat nrformat = NumberFormat.getNumberInstance(Locale.GERMANY);
		double price = nrformat.parse(json.getString("price")).doubleValue();

		SimpleDateFormat format = new SimpleDateFormat("k:mma yyyy-MM-dd");
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(json.getString("time").replace("GMT+1", "") + formatDate.format(new Date()));

		return new Price("DAX", date, price);
	}
}
