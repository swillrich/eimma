package de.elmma.strategies;

import de.elmma.model.Performance;
import de.elmma.model.Price;
import lombok.Data;

@Data
public abstract class Strategy{
	private double invest;

	private Performance performance = new Performance();

	abstract void onUpdate(Price update);
}
