package de.elmma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Data
@Entity
@Table(name = "T_PRICE")
@RequiredArgsConstructor
@NoArgsConstructor
public class Price {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@NonNull
	@Column(name = "underlying", nullable = false)
	private String underlying;
	@NonNull
	@Column(name = "datetime", nullable = false, columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	@NonNull
	@Column(name = "price")
	private double price;
}
