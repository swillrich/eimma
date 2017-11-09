package de.elmma.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Performance extends ArrayList<InvestSnapshot> {
	@Override
	public boolean add(InvestSnapshot e) {
		double change = 0;
		if (!this.isEmpty()) {
			change = (e.getValue() - this.get(0).getValue()) / this.get(0).getValue();
		}
		e.setChangeRatio(change * 100);
		return super.add(e);
	}
}
