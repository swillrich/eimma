package de.elmma.strategies;

import java.util.Arrays;

import de.elmma.dbio.PriceDAO;
import de.elmma.model.Performance;

/**
 * Sucht die angeforderte Strategie und führt sie auf eine bestimmten
 * Kurs-Verlauf aus.
 */
public class StrategyOperator {

	/**
	 * Alle bisher implementierten / lauffähigen Strategien, die per
	 * JSON-Rest-Schnittstelle aufrufbar / anwendbar sein sollen.
	 */
	private static Class<? extends Strategy>[] strategies = new Class[] { TSS.class };

	/**
	 * Wendet eine Strategie an
	 * 
	 * @param strategyName
	 *            Name der Strategie (Name der Klasse, bspw. TSS)
	 */
	public static Performance apply(String strategyName, double invest, String to, String from)
			throws InstantiationException, IllegalAccessException {
		Strategy strategy = Arrays.stream(strategies).filter(s -> s.getSimpleName().equals(strategyName)).iterator()
				.next().newInstance();
		strategy.setInvest(invest);
		PriceDAO.getPrices(from, to).forEach(p -> strategy.nextValue(p));
		return strategy.getPerformance();
	}
}