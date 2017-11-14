package de.elmma.strategies;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.elmma.dbio.PriceDAO;
import de.elmma.model.Performance;
import de.elmma.model.Price;
import de.elmma.strategies.Strategy.StrategyConfiguration;

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

	private static final Logger log = LoggerFactory.getLogger(StrategyOperator.class);

	/**
	 * Wendet eine Strategie an
	 * 
	 * @param strategyName
	 *            Name der Strategie (Name der Klasse, bspw. TSS)
	 */
	public static Performance apply(String strategyName, double invest, String from, String to)
			throws InstantiationException, IllegalAccessException {
		List<Price> prices = PriceDAO.getPrices(from, to);
		Strategy strategy = instantiateStrategy(strategyName);
		strategy.setInvest(invest);
		StrategyConfiguration bestStrategy = searchBestStrategy(strategy, prices);
		applyStrategy(strategy, prices, bestStrategy);
		return strategy.getPerformance();
	}

	private static Strategy instantiateStrategy(String strategyName)
			throws InstantiationException, IllegalAccessException {
		Strategy strategy = Arrays.stream(strategies).filter(s -> s.getSimpleName().equals(strategyName)).iterator()
				.next().newInstance();
		return strategy;
	}

	private static StrategyConfiguration searchBestStrategy(Strategy strategy, List<Price> prices)
			throws InstantiationException, IllegalAccessException {
		log.info("Searching for best strategy");
		StrategyConfiguration best = null;
		double price = 0;
		for (StrategyConfiguration strategyConfiguration : strategy.getConfigurations()) {
			applyStrategy(strategy, prices, strategyConfiguration);
			if (best == null || strategy.getInvest() > price) {
				best = strategyConfiguration;
				price = strategy.getInvest();
			}
		}
		log.info("End of searching");
		return best;
	}

	private static void applyStrategy(Strategy strategy, List<Price> prices,
			StrategyConfiguration strategyConfiguration) {
		strategy.reset();
		String description = strategyConfiguration.configure();
		prices.forEach(p -> strategy.nextValue(p));
		log.info(strategy.getPerformance().get(0).getValue() + " --> " + strategy.getInvest() + " by " + description);
	}

}