package io.github.samuel_pinheiro_c_lopes.patientservice.controllers.dtos;

import io.github.samuel_pinheiro_c_lopes.patientservice.models.Patient;

public record PatientRequestDTO(String cpf, Long personId) {

	public Patient toPatient() {
		return new Patient(personId, cpf);
	}

}
