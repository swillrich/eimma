package de.elmma.strategies;

import java.util.ArrayList;

import de.elmma.model.ElmmaModelFactory;
import de.elmma.model.Performance;
import de.elmma.model.Price;
import de.elmma.model.Snapshot;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 * Eine abstrakte Strategie. Es kann beliebig viele geben.
 */
@Data
public abstract class Strategy {
	/**
	 * Anfangsinvesat
	 */
	@Getter(AccessLevel.NONE)
	private double invest;

	/**
	 * Verlauf entlang des der vorliegenden Kursdaten
	 */
	private Performance performance = ElmmaModelFactory.newPerformance();
	private PriceHistory priceHistory = new PriceHistory();

	/**
	 * Was passiert, wenn ein neuer Preis verfügbar ist.
	 * 
	 * @return
	 */
	abstract Snapshot onUpdate(Price update);

	public double getInvest() {
		return performance.isEmpty() ? invest : performance.getLatest().getValue();
	}

	void nextValue(Price price) {
		priceHistory.add(price.getPrice());
		if (getInvest() >= 0) {
			Snapshot snapshot = onUpdate(price);
			getPerformance().add(snapshot);
		}
	}

	double getLastInvestValue() {
		return getPerformance().isEmpty() ? 0 : getPerformance().getLatest().getValue();
	}

	class PriceHistory extends ArrayList<Double> {
		double getAverage(int until) {
			until = getMin(until);
			return subList(lastIndex() - until, size()).stream().mapToDouble(i -> (double) i).average().getAsDouble();
		}

		private int lastIndex() {
			return size() - 1;
		}

		private int getMin(int until) {
			return until < lastIndex() ? until : lastIndex();
		}

		boolean isAboveAverage(int until) {
			return get(lastIndex() - getMin(until)) > getAverage(until);
		}
	}
}
