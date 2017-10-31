package de.elmma.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "T_TEXT")
public class TEXT extends Demonstrator {
	@Column(name = "sample")
	private String sample;
}
