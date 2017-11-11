package de.elmma.strategies;

import de.elmma.model.Performance;
import de.elmma.model.Price;
import lombok.Data;

/**
 * Eine abstrakte Strategie. Es kann beliebig viele geben.
 */
@Data
public abstract class Strategy {
	/**
	 * Anfangsinvesat
	 */
	private double invest;

	/**
	 * Verlauf entlang des der vorliegenden Kursdaten
	 */
	private Performance performance = new Performance();

	/**
	 * Was passiert, wenn ein neuer Preis verfügbar ist.
	 */
	abstract void onUpdate(Price update);
}
