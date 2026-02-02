package io.github.samuel_pinheiro_c_lopes.patientservice.models;

import io.github.samuel_pinheiro_c_lopes.spring_common.general.enums.AccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name="person_id", nullable = false)
	private Long personId;
	@Column(unique = true, nullable = false)
	private String cpf;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountStatus accountStatus;
	
	public Patient() { }

	public Patient(Long personId, String cpf) {
		super();
		this.personId = personId;
		this.cpf = cpf;
	}

	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
}
