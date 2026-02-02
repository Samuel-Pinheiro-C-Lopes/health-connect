package io.github.samuel_pinheiro_c_lopes.doctorservice.dtos;

import io.github.samuel_pinheiro_c_lopes.doctorservice.models.Doctor;
import io.github.samuel_pinheiro_c_lopes.spring_common.doctor.enums.Specialty;

public record DoctorResponseDTO(
        Long id,
        Long personId,
        String crm,
        Specialty specialty
) {
    public DoctorResponseDTO(final Doctor doctor) {
        this(doctor.getId(), doctor.getPersonId(), doctor.getCrm(), doctor.getSpecialty());
    }
}
