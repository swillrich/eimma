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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "T_PROJECT")
@RequiredArgsConstructor
@NoArgsConstructor
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private int id;
	@NonNull
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	@NonNull
	@Column(name = "logoUrl")
	private String logoUrl;
	@NonNull
	@Column(name = "description", nullable = false, length = 500)
	private String description;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "T_CONSORTIUM", joinColumns = { @JoinColumn(name = "project_id") }, inverseJoinColumns = {
			@JoinColumn(name = "company_id") })
	private List<Company> consortium = new ArrayList<Company>();
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Demonstrator> demonstratoes = new ArrayList<Demonstrator>();
}
