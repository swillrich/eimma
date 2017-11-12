package de.elmma.model;

import lombok.Data;

@Data
public class UninvestSnapshot extends Snapshot {

	Price observes;

	public UninvestSnapshot(Price price, double value) {
		this.observes = price;
		this.value = value;
	}
}
