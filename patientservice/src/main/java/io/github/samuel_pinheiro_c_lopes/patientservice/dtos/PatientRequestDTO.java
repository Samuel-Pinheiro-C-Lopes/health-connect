package io.github.samuel_pinheiro_c_lopes.patientservice.dtos;

import io.github.samuel_pinheiro_c_lopes.patientservice.models.Patient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PatientRequestDTO(
		@NotBlank
		@Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$", message = "Invalid CPF format")
		String cpf,
		@NotNull
		@Min(1)
		Long personId) {

	public Patient toPatient() {
		return new Patient(personId, cpf);
	}

}
