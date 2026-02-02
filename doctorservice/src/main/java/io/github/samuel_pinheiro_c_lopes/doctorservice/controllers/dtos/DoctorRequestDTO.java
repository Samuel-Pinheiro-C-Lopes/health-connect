package io.github.samuel_pinheiro_c_lopes.doctorservice.controllers.dtos;
import io.github.samuel_pinheiro_c_lopes.doctorservice.enums.Specialty;
import io.github.samuel_pinheiro_c_lopes.doctorservice.models.Doctor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record DoctorRequestDTO(
		@NotNull Long personId,
		@NotBlank String crm,
		@NotBlank @Email String email,
		Specialty specialty
) 
{
	public Doctor toDoctor() {
		return new Doctor(personId, crm, email, specialty);
	}
}
