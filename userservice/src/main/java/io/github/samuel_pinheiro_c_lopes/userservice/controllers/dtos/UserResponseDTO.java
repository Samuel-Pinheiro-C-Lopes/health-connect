package io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos;

import io.github.samuel_pinheiro_c_lopes.userservice.models.User;

public record UserResponseDTO(Long id, String email, Long personId) {
	public UserResponseDTO(final User user) {
		this(user.getId(), user.getEmail(), user.getPerson() == null ? null : user.getPerson().getId());
	}
}
