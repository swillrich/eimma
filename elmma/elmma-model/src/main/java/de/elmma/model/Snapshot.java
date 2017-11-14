package de.elmma.model;

import lombok.Data;

@Data
public abstract class Snapshot {
	String kindOf = this.getClass().getSimpleName();
	double value;
	double changeRatio;
}
