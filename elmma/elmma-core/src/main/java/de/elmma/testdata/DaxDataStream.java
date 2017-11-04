package de.elmma.testdata;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import de.elmma.model.Price;

public class DaxDataStream implements Iterable<Price> {
	private Iterable<CSVRecord> records;
	private DateFormat format;

	public DaxDataStream() throws IOException {
		this.format = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.GERMAN);
		Reader in = new FileReader("src/main/resources/traindata/DAX.csv");
		this.records = CSVFormat.DEFAULT.withHeader().parse(in);
	}

	@Override
	public Iterator<Price> iterator() {
		return new Iterator<Price>() {

			@Override
			public boolean hasNext() {
				return DaxDataStream.this.records.iterator().hasNext();
			}

			@Override
			public Price next() {
				CSVRecord record = DaxDataStream.this.records.iterator().next();
				Date date = null;
				try {
					date = format.parse(record.get("Date"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Double lastPrice = Double.valueOf(record.get("LAST_PRICE"));

				Price price = new Price();
				price.setUnderlying("DAX");
				price.setPrice(lastPrice);
				price.setDate(date);

				return price;
			}
		};
	}

}
