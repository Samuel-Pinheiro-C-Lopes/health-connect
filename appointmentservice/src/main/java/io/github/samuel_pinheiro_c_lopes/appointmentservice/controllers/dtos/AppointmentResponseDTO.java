package io.github.samuel_pinheiro_c_lopes.appointmentservice.controllers.dtos;

import io.github.samuel_pinheiro_c_lopes.appointmentservice.models.Appointment;

public record AppointmentResponseDTO() {
	public AppointmentResponseDTO(final Appointment appointment) {
		this();
	}
}
