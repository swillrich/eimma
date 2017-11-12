package de.elmma.strategies;

import de.elmma.model.ElmmaModelFactory;
import de.elmma.model.InvestSnapshot;
import de.elmma.model.KnockOutOption;
import de.elmma.model.Price;
import de.elmma.model.Snapshot;
import de.elmma.model.UninvestSnapshot;

public class TSS extends Strategy {

	@Override
	Snapshot onUpdate(Price update) {
		Snapshot snapshot;
		if (!getPerformance().isEmpty() && getPriceHistory().isAboveAverage(5)) {
			if (getPerformance().getLatest() instanceof UninvestSnapshot) {
				snapshot = addInitial(update);
			} else {
				snapshot = proceed(update);
			}
		} else {
			snapshot = interruptInvest(update);
		}
		return snapshot;
	}

	private Snapshot interruptInvest(Price price) {
		UninvestSnapshot snapshot = ElmmaModelFactory.newUninvestSnapshot(price, getInvest());
		return snapshot;
	}

	private Snapshot proceed(Price update) {
		InvestSnapshot latestSnapshot = (InvestSnapshot) getPerformance().getLatest();
		KnockOutOption latestOption = (KnockOutOption) latestSnapshot.getPrice();
		KnockOutOption newOptionInstance = ElmmaModelFactory.nextKnockOutOption(latestOption, update);
		Snapshot newSnapshot = ElmmaModelFactory.newInvestSnapshot(newOptionInstance, latestSnapshot.getCount());
		return newSnapshot;
	}

	private Snapshot addInitial(Price update) {
		KnockOutOption option = ElmmaModelFactory.newKnockOutOption("WKN123456", update, 55, 1000 * 60 * 60, 1 / 100d);
		Snapshot snapshot = ElmmaModelFactory.newInvestSnapshot(option,
				(int) (getPerformance().getLatest().getValue() / option.getPrice()));
		return snapshot;
	}
}
