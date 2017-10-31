package de.elmma;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.elmma.model.Project;

@RestController
@RequestMapping("/projects")
public class ProjectController {
	// @RequestMapping(value = "/user/", method = RequestMethod.GET)
	@RequestMapping(method = RequestMethod.GET)
	public List<Project> projects(@RequestParam(value = "id", required = false) String ids) {
		String sql = "FROM Project";
		if (ids != null) {
			ids = Arrays.stream(ids.split(",")).filter(s -> s.matches("^-?\\d+$")).collect(Collectors.joining(","));
			sql += " where project_id in (" + ids + ")";
		}
		return QueryExecuter.take(sql, q -> q.list());
	}
}