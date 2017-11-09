package de.elmma.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.elmma.dbio.PriceDAO;
import de.elmma.model.Price;

/**
 * Endpunkt zum Auslesen von Kurswerten
 * @author Danilo.Schmidt
 *
 */
@RestController
@RequestMapping("/prices")
public class PriceController {
	/**
	 * Auslesen als csv-Format (Datum von bis)
	 * @param from
	 * @param to
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/csv", method = RequestMethod.GET, produces = { "text/plain" })
	public String csvPrices(@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		StringBuilder b = new StringBuilder();
		b.append("Datetime,Price\n");
		String csvContent = PriceDAO.getPrices(from, to).stream()
				.map(p -> format.format(p.getDatetime()) + "," + p.getPrice() + "\n").collect(Collectors.joining());
		b.append(csvContent);
		return b.toString();
	}

	/**
	 * Auslesen als JSON-Objekt (Datum von bis)
	 * @param from
	 * @param to
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Price> prices(@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to) throws ParseException {
		return PriceDAO.getPrices(from, to);
	}

}