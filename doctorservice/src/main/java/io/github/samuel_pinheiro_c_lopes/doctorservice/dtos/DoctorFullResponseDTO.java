package io.github.samuel_pinheiro_c_lopes.doctorservice.dtos;

import io.github.samuel_pinheiro_c_lopes.spring_common.doctor.enums.Specialty;

public record DoctorFullResponseDTO(
        Long id,
        Long userId,
        String crm,
        Specialty specialty,
		String name,
		String phone,
		String postalCode, 
		String avenue, 
		String complement, 
		String number, 
		String city, 
		String district, 
		String state
) { }
