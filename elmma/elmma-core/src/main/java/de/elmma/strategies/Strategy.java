package de.elmma.strategies;

import de.elmma.model.Performance;
import de.elmma.model.Price;
import lombok.Data;

@Data
public abstract class Strategy {
	private Performance performance;

	abstract double getInitialInvest();

	abstract void onUpdate(Price update);
}
