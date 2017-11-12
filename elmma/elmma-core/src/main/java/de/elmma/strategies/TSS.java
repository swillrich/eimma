package de.elmma.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import de.elmma.model.ElmmaModelFactory;
import de.elmma.model.InvestSnapshot;
import de.elmma.model.KnockOutOption;
import de.elmma.model.Price;
import de.elmma.model.Snapshot;
import de.elmma.model.UninvestSnapshot;

public class TSS extends Strategy {

	private int averageDistance;
	private int knockoutDistance;

	@Override
	Snapshot onUpdate(Price update) {
		Snapshot snapshot;
		if (!getPerformance().isEmpty() && getPriceHistory().isAboveAverage(averageDistance)) {
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
		KnockOutOption latestOption = (KnockOutOption) latestSnapshot.getInvestedIn();
		KnockOutOption newOptionInstance = ElmmaModelFactory.nextKnockOutOption(latestOption, update);
		Snapshot newSnapshot = ElmmaModelFactory.newInvestSnapshot(newOptionInstance, latestSnapshot.getCount());
		return newSnapshot;
	}

	private Snapshot addInitial(Price update) {
		KnockOutOption option = ElmmaModelFactory.newKnockOutOption("WKN123456", update, knockoutDistance,
				1000 * 60 * 60, 1 / 100d);
		Snapshot snapshot = ElmmaModelFactory.newInvestSnapshot(option,
				(int) (getPerformance().getLatest().getValue() / option.getPrice()));
		return snapshot;
	}

	@Override
	public List<StrategyConfiguration> getConfigurations() {
		return new ArrayList<StrategyConfiguration>() {
			AtomicInteger lastChangesForAverage;
			AtomicInteger distanceToKnockOut = new AtomicInteger(10);
			{
				for (; distanceToKnockOut.getAndAdd(20) < 500;) {
					lastChangesForAverage = new AtomicInteger(0);
					for (; lastChangesForAverage.getAndIncrement() <= 100;) {
						int lastChangesForAverageValue = lastChangesForAverage.get();
						int distanceToKnockoutValue = distanceToKnockOut.get();
						add(() -> {
							TSS.this.averageDistance = lastChangesForAverageValue;
							TSS.this.knockoutDistance = distanceToKnockoutValue;
							return "Profit by average distance " + lastChangesForAverageValue
									+ " and KnockOut distance: " + distanceToKnockoutValue;
						});
					}
				}
			}
		};
	}

}
