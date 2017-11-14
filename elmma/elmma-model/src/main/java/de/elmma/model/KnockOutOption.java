package de.elmma.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.classic.Lifecycle;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Entity
@Table(name = "T_KNOCKOUTOPTION")
public class KnockOutOption extends Price implements Lifecycle {
	@ManyToOne
	@JoinColumn(name = "trackedUnderlying")
	Price trackedUnderlying;
	@ManyToOne
	@JoinColumn(name = "knockoutBarrier")
	Price knockoutBarrier;
	@Column(name = "multiplier", nullable = false)
	double multiplier;
	@Column(name = "laverage", nullable = false)
	double laverage;
	@Column(name = "credit", nullable = false)
	double credit;

	public KnockOutOption(String underlying, Price trackedUnderlying, Price knockOutBarrier, double multiplier) {
		this.underlying = underlying;
		this.trackedUnderlying = trackedUnderlying;
		this.knockoutBarrier = knockOutBarrier;
		this.multiplier = multiplier;
		this.datetime = trackedUnderlying.getDatetime();
		this.price = (trackedUnderlying.getPrice() - knockOutBarrier.getPrice()) * multiplier;
		this.credit = trackedUnderlying.getPrice() - price;
		this.laverage = (trackedUnderlying.getPrice() * multiplier) / price;
		this.initialize();
	}
}