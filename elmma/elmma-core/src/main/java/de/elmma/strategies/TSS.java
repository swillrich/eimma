package de.elmma.strategies;

import de.elmma.model.Price;

public class TSS extends Strategy {

	@Override
	public double getInitialInvest() {
		return 1000d;
	}

	@Override
	void onUpdate(Price update) {
		
	}
}
