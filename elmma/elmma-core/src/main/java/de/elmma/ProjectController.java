package de.elmma;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.elmma.model.Price;

@RestController
@RequestMapping("/prices")
public class ProjectController {
	@RequestMapping(method = RequestMethod.GET)
	public List<Price> projects() {
		return QueryExecuter.take("FROM Price", q -> q.list());
	}
}