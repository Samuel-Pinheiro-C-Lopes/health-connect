package io.github.samuel_pinheiro_c_lopes.doctorservice.models;
import io.github.samuel_pinheiro_c_lopes.doctorservice.enums.Specialty;
import io.github.samuel_pinheiro_c_lopes.doctorservice.enums.StatusSolicitacao;
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
	private String email;
	@Column(name = "status_solicitacao", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusSolicitacao statusSolicitacao = StatusSolicitacao.PENDENTE;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Specialty specialty;
	public Doctor() {
		
	}
	public Doctor(final Long personId, final String crm, final String email, final Specialty specialty) {
		this.personId = personId;
		this.crm = crm;
		this.email = email;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public StatusSolicitacao getStatusSolicitacao() {
		return statusSolicitacao;
	}
	public void setStatusSolicitacao(final StatusSolicitacao statusSolicitacao) {
		this.statusSolicitacao = statusSolicitacao;
	}

	public Specialty getSpecialty() {
		return specialty;
	}
	public void setSpecialty(final Specialty specialty) {
		this.specialty = specialty;
	}
	
}