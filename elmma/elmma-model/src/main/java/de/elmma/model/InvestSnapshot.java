package de.elmma.model;

import lombok.Data;

@Data
public class InvestSnapshot extends Snapshot {

	Price investedIn;
	double count;

	public InvestSnapshot(Price price, double count) {
		this.investedIn = price;
		this.count = count;
		if (price != null) {
			this.value = price.getPrice() * count;
		}
	}
}
