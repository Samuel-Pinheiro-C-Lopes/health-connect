package io.github.samuel_pinheiro_c_lopes.doctorservice.controllers.dtos;

import io.github.samuel_pinheiro_c_lopes.doctorservice.models.Doctor;

public record DoctorResponseDTO(
        Long id,
        Long personId,
        String crm,
        String specialty
) {
    public DoctorResponseDTO(final Doctor doctor) {
        this(doctor.getId(), doctor.getPersonId(), doctor.getCrm(), doctor.getSpecialty());
    }
}
