package de.elmma.model;

import java.util.Date;

public class ElmmaModelFactory {
	public static KnockOutOption newKnockOutOption(String name, Price trackedUnderlying, double distance,
			int timeBefore, double multiplier) {
		Date datetime = new Date(trackedUnderlying.getDatetime().getTime() - timeBefore);
		Price knockOutBarrier = new Price(trackedUnderlying.getUnderlying(), datetime,
				trackedUnderlying.getPrice() - distance);
		return new KnockOutOption(name, trackedUnderlying, knockOutBarrier, multiplier);
	}

	public static KnockOutOption nextKnockOutOption(KnockOutOption previous, Price trackedUnderlying) {
		KnockOutOption option = new KnockOutOption(trackedUnderlying.getUnderlying(), trackedUnderlying,
				previous.getKnockoutBarrier(), previous.getMultiplier());
		option.setPrevious(previous);
		return option;
	}

	public static Price newPrice(String underlying, Date datetime, double price) {
		return new Price(underlying, datetime, price);
	}

	public static Snapshot newInvestSnapshot(KnockOutOption newOptionInstance, double count) {
		return new InvestSnapshot(newOptionInstance, count);
	}

	public static Performance newPerformance() {
		return new Performance();
	}

	public static UninvestSnapshot newUninvestSnapshot(Price price, double value) {
		return new UninvestSnapshot(price, value);
	}
}
