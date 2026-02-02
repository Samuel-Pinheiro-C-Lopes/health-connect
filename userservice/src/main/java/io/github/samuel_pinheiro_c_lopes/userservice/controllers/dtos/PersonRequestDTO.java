package io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos;

import io.github.samuel_pinheiro_c_lopes.userservice.models.Person;

public record PersonRequestDTO(
		String name,
		String phone,
		Long userId,
		String postalCode, 
		String avenue, 
		String complement, 
		String number, 
		String city, 
		String district, 
		String state
) {
	public Person toPerson() {
		return new Person(
				name(), 
				phone(),
				userId(),
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
