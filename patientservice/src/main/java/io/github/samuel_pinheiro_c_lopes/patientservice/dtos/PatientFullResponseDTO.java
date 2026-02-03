package io.github.samuel_pinheiro_c_lopes.patientservice.dtos;

public record PatientFullResponseDTO(
		Long id, 
		Long userId,
		String name,
		String email,
		String phone,
		String postalCode, 
		String avenue, 
		String complement, 
		String number, 
		String city, 
		String district, 
		String state
) { }
