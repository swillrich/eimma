package de.elmma.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "T_COMPANY")
@RequiredArgsConstructor
@NoArgsConstructor
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@NonNull
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "address")
	private String address;
	@Column(name = "zipcode")
	private String zipcode;
	@NonNull
	@Column(name = "city")
	private String city;
	@NonNull
	@Column(name = "country")
	private String country;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "T_COMPANY_ASSOCIATED_USER", joinColumns = {
			@JoinColumn(name = "company_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	private List<User> associatedUsers = new ArrayList<User>();
}
