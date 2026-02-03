package io.github.samuel_pinheiro_c_lopes.userservice.dtos.user;

import io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos.CommonUserResponseDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.models.User;

public record UserResponseDTO(
		Long id, 
		Long patientId,
		Long doctorId,
		String email, 
		String name,
		String phone,
		String postalCode, 
		String avenue, 
		String complement, 
		String number, 
		String city, 
		String district, 
		String state
) implements CommonUserResponseDTO {
	public UserResponseDTO(final User user) {
		this(
			user.getId(), 
			user.getPatientId(),
			user.getDoctorId(),
			user.getEmail(), 
			user.getName(),
			user.getPhone(),
			user.getAddress().getPostalCode(),
			user.getAddress().getAvenue(),
			user.getAddress().getComplement(),
			user.getAddress().getNumber(),
			user.getAddress().getCity(),
			user.getAddress().getDistrict(),
			user.getAddress().getState()
		);
	}
}
