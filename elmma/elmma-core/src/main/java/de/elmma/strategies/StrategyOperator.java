package de.elmma.strategies;

import java.util.Arrays;

import de.elmma.dbio.PriceDAO;
import de.elmma.model.Performance;

public class StrategyOperator {

	private static Class<? extends Strategy>[] strategies = new Class[] { TSS.class };

	public static Performance apply(String strategyName, double invest, String to, String from)
			throws InstantiationException, IllegalAccessException {
		Strategy strategy = Arrays.stream(strategies).filter(s -> s.getSimpleName().equals(strategyName)).iterator().next()
				.newInstance();
		strategy.setInvest(invest);
		PriceDAO.getPrices(from, to).forEach(p -> strategy.onUpdate(p));
		return strategy.getPerformance();
	}
}
