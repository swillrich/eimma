package de.elmma.strategies;

import de.elmma.model.InvestSnapshot;
import de.elmma.model.KnockOutOption;
import de.elmma.model.Price;

public class TSS extends Strategy {

	@Override
	void onUpdate(Price update) {
		if (getPerformance().isEmpty()) {
			addInitial(update);
		}
		proceed(update);
	}

	private void proceed(Price update) {
		InvestSnapshot snapshot = getPerformance().get(getPerformance().size() - 1);
		KnockOutOption option = (KnockOutOption) snapshot.getPrice();
		KnockOutOption newOptionInstance = new KnockOutOption(option, update);
		if (newOptionInstance.getPrice() < 0) {
			return;
		}
		InvestSnapshot newSnapshot = new InvestSnapshot(newOptionInstance, snapshot.getCount());
		getPerformance().add(newSnapshot);
	}

	private void addInitial(Price update) {
		KnockOutOption option = new KnockOutOption("WKN123456", update, update, 2, 1 / 100d);
		int count = (int) (getInvest() / option.getPrice());
		InvestSnapshot snapshot = new InvestSnapshot(option, count);
		getPerformance().add(snapshot);
	}
}
