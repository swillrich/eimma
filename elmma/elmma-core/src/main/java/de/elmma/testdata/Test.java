package de.elmma.testdata;

import java.io.IOException;

import de.elmma.dbio.SessionProvider;
import de.elmma.model.Price;

public class Test {

	public Test() {
		try {
			final DaxDataStream stream = new DaxDataStream();
			SessionProvider.save(session -> {
				for (Price price : stream) {
					session.save(price);
				}
				return null;
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
