package de.elmma.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "T_USER")
@RequiredArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@NonNull
	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;
	@NonNull
	@Column(name = "prename", length = 100)
	private String prename;
	@NonNull
	@Column(name = "surname", length = 100)
	private String surname;
	@NonNull
	@Column(name = "mail", length = 255)
	private String mail;
	@Column(name = "password")
	private String password;
	@Column(name = "salt")
	private String salt;
}
