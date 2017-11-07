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
public class IntradayFetcher {

	private static final Logger log = LoggerFactory.getLogger(IntradayFetcher.class);

	SimpleDateFormat format = new SimpleDateFormat("dd. MMM yyyy HH:mm");

	Price lastPrice = null;

	public IntradayFetcher() {
		lastPrice = SessionProvider.take("FROM Price p ORDER BY p.datetime DESC", q -> {
			q.setMaxResults(1);
			return q.uniqueResult();
		});
		log.info("####### take the last price: " + lastPrice);
		System.out.println("####### take the last price: " + lastPrice);
	}

	@Scheduled(fixedRate = 1000)
	private void requestNewPrice() {
		try {
			Price price = fetchCurrentPrice();
			if (price.getPrice() != price.getPrice()) {
				SessionProvider.save(session -> session.save(price));
				lastPrice = price;
				log.info("####### saved new price: " + lastPrice);
				System.out.println("####### saved new price: " + lastPrice);
			}
			Thread.sleep(1000);
		} catch (ParseException | InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	private Price fetchCurrentPrice() throws IOException, ParseException {
		JSONObject json = JSONURLReader.readJsonFromUrl("http://elmma-exchange-api:8080/snapshot");
		NumberFormat nrformat = NumberFormat.getNumberInstance(Locale.GERMANY);
		double price = nrformat.parse(json.getString("price")).doubleValue();
		return new Price("DAX", new Date(), price);
	}
}
