package de.elmma.model;

import lombok.Data;

@Data
public class InvestSnapshot {

	Price price;
	double value;
	double count;
	double changeRatio;

	public InvestSnapshot(Price price, double count) {
		this.price = price;
		this.count = count;
		this.value = price.getPrice() * count;
	}
}
