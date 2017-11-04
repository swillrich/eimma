package de.elmma.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.elmma.dbio.SessionProvider;
import de.elmma.model.Price;

@RestController
@RequestMapping("/prices")
public class PriceController {
	@RequestMapping(method = RequestMethod.GET)
	public List<Price> projects() {
		return SessionProvider.take("FROM Price", q -> q.list());
	}
}