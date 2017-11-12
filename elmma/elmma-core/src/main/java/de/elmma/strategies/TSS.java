package de.elmma.strategies;

import de.elmma.model.ElmmaModelFactory;
import de.elmma.model.InvestSnapshot;
import de.elmma.model.KnockOutOption;
import de.elmma.model.Price;

public class TSS extends Strategy {

	@Override
	void onUpdate(Price update) {
		if (getLastInvestValue() < 0) {
			return;
		}
		if (getPerformance().isEmpty()) {
			addInitial(update);
		} else {
			proceed(update);
		}
	}

	private void proceed(Price update) {
		InvestSnapshot latestSnapshot = getPerformance().getLatest();
		KnockOutOption latestOption = (KnockOutOption) latestSnapshot.getPrice();
		KnockOutOption newOptionInstance = ElmmaModelFactory.nextKnockOutOption(latestOption, update);
		InvestSnapshot newSnapshot = ElmmaModelFactory.newInvestSnapshot(newOptionInstance, latestSnapshot.getCount());
		getPerformance().add(newSnapshot);
	}

	private void addInitial(Price update) {
		KnockOutOption option = ElmmaModelFactory.newKnockOutOption("WKN123456", update, 55, 1000 * 60 * 60, 1 / 100d);
		InvestSnapshot snapshot = ElmmaModelFactory.newInvestSnapshot(option, (int) (getInvest() / option.getPrice()));
		getPerformance().add(snapshot);
	}
}
