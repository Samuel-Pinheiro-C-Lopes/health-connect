package io.github.samuel_pinheiro_c_lopes.userservice.dtos;

import java.util.List;

import io.github.samuel_pinheiro_c_lopes.userservice.models.Role;
import io.github.samuel_pinheiro_c_lopes.userservice.models.User;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
		@NotBlank
		String email,
		@NotBlank
		String password,
		List<Long> roles
){
	public User toUser() {
		return new User(
				roles()
				.stream()
				.map(Role::new)
				.toList(),
				email(), 
				password()
		);
	}
}
