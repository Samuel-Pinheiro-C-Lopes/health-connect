package io.github.samuel_pinheiro_c_lopes.patientservice.dtos;

import io.github.samuel_pinheiro_c_lopes.patientservice.models.Patient;
import io.github.samuel_pinheiro_c_lopes.spring_common.patient.dtos.CommonPatientResponseDTO;

public record PatientResponseDTO(Long id, Long personId, String cpf) {

	public PatientResponseDTO(Patient patient) {
		this(
			patient.getId(),
			patient.getPersonId(),
			patient.getCpf()
		);
	}

}
