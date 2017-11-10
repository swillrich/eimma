package de.elmma.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.elmma.model.Performance;
import de.elmma.strategies.StrategyOperator;

/**
 * Führt eine Strategie auf eine bestimmte Dauer mit einem bestimmten Invest aus
 * und gibt das Ergebnis (JSON) zurück.
 */
@RestController
@RequestMapping("/strategy")
public class StrategyController {
	/**
	 * @param from
	 *            Zeitraum von (yyyy-MM-dd)
	 * @param to
	 *            Zeitraum bis (yyyy-MM-dd)
	 * @param invest
	 *            Gesamtinvest (bspw. 1000,00)
	 * @param strategy
	 *            Name der Strategie (bspw. TSS)
	 * @return
	 * @throws ParseException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Performance prices(@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to,
			@RequestParam(value = "invest", required = false) double invest,
			@RequestParam(value = "s", required = false) String strategy)
			throws ParseException, InstantiationException, IllegalAccessException {
		return StrategyOperator.apply(strategy, invest, from, to);
	}
}