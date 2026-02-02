package io.github.samuel_pinheiro_c_lopes.userservice.dtos.user;

import io.github.samuel_pinheiro_c_lopes.userservice.models.User;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
		@NotBlank
		String email,
		@NotBlank
		String password

){
	public User toUser() {
		return new User(
				email(), 
				password()
		);
	}
}
