package io.github.samuel_pinheiro_c_lopes.doctorservice.clients.dtos;
public record PersonResponseDTO(
		Long id,
		String name,
		String phone,
		Long userId,
		Long patientId,
		Long doctorId,
		String postalCode,
		String avenue,
		String complement,
		String number,
		String city,
		String district,
		String state
) { }
