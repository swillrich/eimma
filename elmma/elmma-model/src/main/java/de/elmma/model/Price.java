package de.elmma.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * ein Kurswert-Datensazt Objekt
 * @author Danilo.Schmidt
 *
 */
@ToString
@Data
@Entity
@Table(name = "T_PRICE")
@RequiredArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Price implements Lifecycle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	int id;
	@NonNull
	@Column(name = "underlying", nullable = false)
	String underlying;
	@NonNull
	@Column(name = "datetime", nullable = false, columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	Date datetime;
	@NonNull
	@Column(name = "price")
	double price;

	@Transient
	double priceChange;
	@Transient
	double priceChangeRatio;
	@Transient
	String readableDateTime;
	
	public void setPrevious(Price previous) {
		this.priceChange = this.price - previous.getPrice();
		this.priceChangeRatio = this.priceChange / this.price;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		initialize();
	}

	public void initialize() {
		this.readableDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getDatetime());
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}
}
