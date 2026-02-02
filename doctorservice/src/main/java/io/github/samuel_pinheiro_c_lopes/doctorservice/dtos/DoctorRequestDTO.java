package io.github.samuel_pinheiro_c_lopes.doctorservice.dtos;

import io.github.samuel_pinheiro_c_lopes.doctorservice.models.Doctor;
import io.github.samuel_pinheiro_c_lopes.spring_common.doctor.enums.Specialty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DoctorRequestDTO(
		@NotNull Long personId,
		@NotBlank String crm,
		Specialty specialty
) {
	public Doctor toDoctor() {
		return new Doctor(personId, crm, specialty);
	}
}
