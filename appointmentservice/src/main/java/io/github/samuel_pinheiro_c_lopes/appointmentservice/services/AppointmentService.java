package io.github.samuel_pinheiro_c_lopes.appointmentservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.samuel_pinheiro_c_lopes.appointmentservice.controllers.dtos.AppointmentRequestDTO;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.controllers.dtos.AppointmentResponseDTO;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.models.Appointment;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.repositories.AppointmentRepository;

@Service
public class AppointmentService {
	private final AppointmentRepository appointmentRepository;

	@Autowired
	public AppointmentService(final AppointmentRepository appointmentRepository) {
		this.appointmentRepository = appointmentRepository;
	}
	
	public AppointmentResponseDTO save(final AppointmentRequestDTO userRequest) {
		final Appointment appointment = userRequest.toAppointment();

		return new AppointmentResponseDTO(this.appointmentRepository.save(appointment));
	}
	
	public List<AppointmentResponseDTO> findAll() {
		return this.appointmentRepository.findAll()
				.stream()
				.map(u -> new AppointmentResponseDTO(u))
				.toList();
	}
	
	public AppointmentResponseDTO update(final Long id, final AppointmentRequestDTO userRequest) {
		final Appointment toBeUpdatedAppointment = this.appointmentRepository.getReferenceById(id);
		
		return new AppointmentResponseDTO(this.appointmentRepository.save(toBeUpdatedAppointment));
	}
	
	public void delete(final Long id) {
		this.appointmentRepository.delete(this.appointmentRepository.getReferenceById(id));
	}
}
