package de.elmma.model;

import java.util.List;

import lombok.Data;

@Data
public class Performance {
	int count;
	List<Price> history;
}
