package io.github.samuel_pinheiro_c_lopes.spring_common.doctor.dtos;

import io.github.samuel_pinheiro_c_lopes.spring_common.doctor.enums.Specialty;

public interface CommonDoctorResponseDTO {
    Long id();
    Long personId();
    String crm();
    Specialty specialty();
}
