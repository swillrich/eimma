package de.elmma.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.elmma.model.Performance;
import de.elmma.strategies.StrategyOperator;

@RestController
@RequestMapping("/strategy")
public class StrategyController {
	@RequestMapping(method = RequestMethod.GET)
	public Performance prices(@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to,
			@RequestParam(value = "invest", required = false) double invest,
			@RequestParam(value = "s", required = false) String strategy)
			throws ParseException, InstantiationException, IllegalAccessException {
		return StrategyOperator.apply(strategy, invest, from, to);
	}
}