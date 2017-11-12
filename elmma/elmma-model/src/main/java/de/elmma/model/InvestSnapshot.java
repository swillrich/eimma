package de.elmma.model;

import lombok.Data;

@Data
public class InvestSnapshot extends Snapshot {

	Price price;
	double count;

	public InvestSnapshot(Price price, double count) {
		this.price = price;
		this.count = count;
		if (price != null) {
			this.value = price.getPrice() * count;
		}
	}
}
