package io.github.samuel_pinheiro_c_lopes.patientservice.controllers.dtos;

import io.github.samuel_pinheiro_c_lopes.patientservice.clients.dtos.PersonResponseDTO;

public record PatientDetailsResponseDTO(
		PatientResponseDTO patient,
		PersonResponseDTO person
) { }
