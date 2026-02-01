package io.github.samuel_pinheiro_c_lopes.doctorservice.models;

import io.github.samuel_pinheiro_c_lopes.doctorservice.enums.RegisterRequestStatus;
import io.github.samuel_pinheiro_c_lopes.doctorservice.enums.Specialty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Specialty specialty;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private RegisterRequestStatus registerRequestStatus;
	
	public Doctor() {
		// JPA
	}

	public Doctor(final Long personId, final String crm, final Specialty specialty, RegisterRequestStatus registerRequestStatus) {
		this.personId = personId;
		this.crm = crm;
		this.specialty = specialty;
		this.registerRequestStatus = registerRequestStatus;
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

	public Specialty getSpecialty() {
		return specialty;
	}

	public void setSpecialty(final Specialty specialty) {
		this.specialty = specialty;
	}
	
	public RegisterRequestStatus getRegisterRequestStatus() {
		return registerRequestStatus;
	}

	public void setSpecialty(final RegisterRequestStatus registerRequestStatus) {
		this.registerRequestStatus = registerRequestStatus;
	}
}
