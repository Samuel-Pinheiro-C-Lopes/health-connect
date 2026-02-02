package io.github.samuel_pinheiro_c_lopes.doctorservice.controllers.dtos;
import io.github.samuel_pinheiro_c_lopes.doctorservice.clients.dtos.PersonResponseDTO;
public record DoctorDetailsResponseDTO(
		DoctorResponseDTO doctor,
		PersonResponseDTO person
) { }