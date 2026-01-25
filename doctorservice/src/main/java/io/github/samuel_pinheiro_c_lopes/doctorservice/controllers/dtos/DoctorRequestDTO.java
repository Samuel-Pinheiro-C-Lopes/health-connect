package io.github.samuel_pinheiro_c_lopes.doctorservice.controllers.dtos;

import io.github.samuel_pinheiro_c_lopes.doctorservice.models.Doctor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DoctorRequestDTO(
		@NotNull Long personId,
		@NotBlank String crm,
		String specialty
) {
	public Doctor toDoctor() {
		return new Doctor(personId, crm, specialty);
	}
}
