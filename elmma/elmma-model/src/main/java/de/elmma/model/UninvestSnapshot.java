package de.elmma.model;

import lombok.Data;

@Data
public class UninvestSnapshot extends Snapshot {

	Price price;

	public UninvestSnapshot(Price price, double value) {
		this.price = price;
		this.value = value;
	}
}
