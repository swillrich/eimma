package de.elmma.model;

import java.util.ArrayList;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Performance extends ArrayList<Snapshot> {
	@Override
	public boolean add(Snapshot e) {
		double change = 0;
		if (!this.isEmpty()) {
			change = (e.getValue() - this.get(0).getValue()) / this.get(0).getValue();
		}
		e.setChangeRatio(change * 100);
		return super.add(e);
	}

	public Snapshot getLatest() {
		return this.size() == 0 ? null : get(this.size() - 1);
	}
}