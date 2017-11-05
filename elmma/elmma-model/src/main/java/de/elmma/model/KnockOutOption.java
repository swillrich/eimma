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

	public KnockOutOption(String underlyingName, Price trackedUnderlying, Price knockoutBarrier, double price,
			double multiplier) {
		set(underlyingName, trackedUnderlying, knockoutBarrier, price, multiplier,
				trackedUnderlying.getPrice() - price);
	}

	private void set(String underlyingName, Price trackedUnderlying, Price knockoutBarrier, double price,
			double multiplier, double credit) {
		this.underlying = underlyingName;
		this.datetime = trackedUnderlying.getDatetime();
		this.price = price;
		this.trackedUnderlying = trackedUnderlying;
		this.knockoutBarrier = knockoutBarrier;
		this.multiplier = multiplier;
		this.laverage = (trackedUnderlying.getPrice() * multiplier) / price;
		this.credit = credit;
	}

	public KnockOutOption(KnockOutOption previous, Price trackedUnderlying) {
		this.priceChangeRatio = previous.getLaverage() * trackedUnderlying.getPriceChangeRatio();
		this.priceChange = previous.getPrice() * this.priceChangeRatio;
		double newPrice = previous.getPrice() + priceChange;
		set(previous.getUnderlying(), trackedUnderlying, previous.getKnockoutBarrier(), newPrice,
				previous.getMultiplier(), previous.getCredit());
	}
}