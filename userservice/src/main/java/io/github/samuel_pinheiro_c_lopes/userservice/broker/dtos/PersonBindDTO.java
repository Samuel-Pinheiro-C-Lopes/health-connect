package io.github.samuel_pinheiro_c_lopes.userservice.broker.dtos;

public record PersonBindDTO(
		Long personId, 
		Long doctorId, 
		Long patientId
) { }
