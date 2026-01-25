package io.github.samuel_pinheiro_c_lopes.patientservice.controllers.dtos;

import io.github.samuel_pinheiro_c_lopes.patientservice.models.Patient;

public record PatientResponseDTO(Long id, Long personId, String cpf) {

	public PatientResponseDTO(Patient patient) {
		this(
			patient.getId(),
			patient.getPersonId(),
			patient.getCpf()
		);
	}

}
