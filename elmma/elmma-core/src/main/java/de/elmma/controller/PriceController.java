package de.elmma.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.elmma.dbio.SessionProvider.HibernateSessionProvider;
import de.elmma.model.Price;

@RestController
@RequestMapping("/prices")
public class PriceController {
	@RequestMapping(method = RequestMethod.GET)
	public List<Price> prices(@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to) throws ParseException {
		return (List<Price>) new HibernateSessionProvider() {

			@Override
			public Object work(Session session) {
				Criteria criteria = session.createCriteria(Price.class);
				try {
					if (from != null) {
						criteria.add(Restrictions.sqlRestriction("DATE(datetime) >= ?", extractDate(from),
								DateType.INSTANCE));
					}
					if (to != null) {
						criteria.add(
								Restrictions.sqlRestriction("DATE(datetime) <= ?", extractDate(to), DateType.INSTANCE));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return criteria.list();
			}

			private Date extractDate(String strDate) throws ParseException {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				System.out.println("######### -> " + to + " ---> " + format.parse(strDate));
				return format.parse(strDate);
			}
		}.getResult();
	}
}