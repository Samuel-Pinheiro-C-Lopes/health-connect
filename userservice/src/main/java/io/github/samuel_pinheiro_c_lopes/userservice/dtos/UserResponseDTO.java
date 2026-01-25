package io.github.samuel_pinheiro_c_lopes.userservice.dtos;

import io.github.samuel_pinheiro_c_lopes.userservice.models.User;

public record UserResponseDTO(String email, Long personId) {
	public UserResponseDTO(final User user) {
		this(user.getEmail(), user.getPerson() == null ? null : user.getPerson().getId());
	}
}
