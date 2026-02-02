package io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos;

import io.github.samuel_pinheiro_c_lopes.userservice.models.Role;

public record RoleResponseDTO(Long id, String name) {
	public RoleResponseDTO(final Role role) {
		this(role.getId(), role.getAuthority());
	}
}
