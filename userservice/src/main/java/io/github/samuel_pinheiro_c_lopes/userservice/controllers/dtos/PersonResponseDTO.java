package io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos;

import io.github.samuel_pinheiro_c_lopes.userservice.models.Person;

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
) {
	public PersonResponseDTO(final Person person) {
		this(
			person.getId(),
			person.getName(),
			person.getPhone(),
			person.getUser().getId(),
			person.getPatientId(),
			person.getDoctorId(),
			person.getAddress().getPostalCode(),
			person.getAddress().getAvenue(),
			person.getAddress().getComplement(),
			person.getAddress().getNumber(),
			person.getAddress().getCity(),
			person.getAddress().getDistrict(),
			person.getAddress().getState()
		);
	}
}
