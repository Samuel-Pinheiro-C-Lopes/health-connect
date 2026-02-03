package io.github.samuel_pinheiro_c_lopes.userservice.dtos.person;

import io.github.samuel_pinheiro_c_lopes.userservice.models.Person;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PersonRequestDTO(
		@NotBlank
		String name,
		@NotNull
		@NotBlank
		@Pattern(regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}$", message = "Invalid phone format")
		String phone,
		@NotBlank
		@Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "Invalid CEP format")
		String postalCode,
		@NotBlank
		String avenue,
		@NotBlank
		String complement,
		@NotBlank
		String number,
		@NotBlank
		String city,
		@NotBlank
		String district, 
		@NotBlank
		String state
) {
	public Person toPerson() {
		return new Person(
				name(), 
				phone(),
				city(),
				district(),
				state(),
				postalCode(),
				avenue(),
				number(),
				complement()
				
		);
	}
}
