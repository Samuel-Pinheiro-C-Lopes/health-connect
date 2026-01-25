package io.github.samuel_pinheiro_c_lopes.doctorservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Doctor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "person_id", nullable = false)
	private Long personId;

	@Column(nullable = false, unique = true)
	private String crm;

	@Column(nullable = true)
	private String specialty;

	public Doctor() {
		// JPA
	}

	public Doctor(final Long personId, final String crm, final String specialty) {
		this.personId = personId;
		this.crm = crm;
		this.specialty = specialty;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(final Long personId) {
		this.personId = personId;
	}

	public String getCrm() {
		return crm;
	}

	public void setCrm(final String crm) {
		this.crm = crm;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(final String specialty) {
		this.specialty = specialty;
	}
}
