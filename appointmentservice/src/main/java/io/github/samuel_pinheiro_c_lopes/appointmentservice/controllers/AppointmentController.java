package io.github.samuel_pinheiro_c_lopes.appointmentservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.samuel_pinheiro_c_lopes.appointmentservice.dtos.AppointmentCancelDTO;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.dtos.AppointmentFullResponseDTO;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.dtos.AppointmentRequestDTO;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.dtos.AppointmentResponseDTO;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.services.AppointmentService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
	private final AppointmentService appointmentService;
	
	@Autowired
	public AppointmentController(final AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority(@rolesConfiguration.admin) or hasAuthority(@rolesConfiguration.manager) or hasAuthority(@rolesConfiguration.doctor) or hasAuthority(@rolesConfiguration.patient)")
	public ResponseEntity<List<AppointmentResponseDTO>> findAll() {
		return ResponseEntity.ok(this.appointmentService.findAll());
	}
	
	@GetMapping("/patient/currentlyLoggedIn")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<AppointmentFullResponseDTO>> findAllFromPatientCurrentlyLoggedIn() {
		return ResponseEntity.ok(this.appointmentService.findAllFromPatientCurrentlyLoggedIn());
	}
	
	@GetMapping("/doctor/currentlyLoggedIn")
	@PreAuthorize("hasAnyAuthority(@rolesConfiguration.admin, @rolesConfiguration.manager, @rolesConfiguration.doctor)")
	public ResponseEntity<List<AppointmentFullResponseDTO>> findAllFromDoctorCurrentlyLoggedIn() {
		return ResponseEntity.ok(this.appointmentService.findAllFromDoctorCurrentlyLoggedIn());
	}
	
	@GetMapping("/patient/{patientId}")
	@PreAuthorize("hasAnyAuthority(@rolesConfiguration.admin, @rolesConfiguration.manager, @rolesConfiguration.doctor)")
	public ResponseEntity<List<AppointmentFullResponseDTO>> findAllFromPatient(@PathVariable final Long patientId) {
		return ResponseEntity.ok(this.appointmentService.findAllFromPatient(patientId));
	}
	
	@GetMapping("/doctor/{doctorId}")
	@PreAuthorize("hasAnyAuthority(@rolesConfiguration.admin, @rolesConfiguration.manager, @rolesConfiguration.doctor)")
	public ResponseEntity<List<AppointmentFullResponseDTO>> findAllFromDoctor(@PathVariable final Long doctorId) {
		return ResponseEntity.ok(this.appointmentService.findAllFromDoctor(doctorId));
	}
	
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<AppointmentResponseDTO> save(
			@RequestBody final AppointmentRequestDTO userRequest
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.appointmentService.save(userRequest));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<AppointmentResponseDTO> update(
			@PathVariable final Long id, 
			@RequestBody final AppointmentRequestDTO appointmentRequest
	) {
		return ResponseEntity.ok(this.appointmentService.update(id, appointmentRequest));
	}
	
	@PatchMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<AppointmentResponseDTO> patch(
			@PathVariable final Long id,
			@RequestBody final AppointmentCancelDTO appointmentCancellation
	) {
		return ResponseEntity.ok(this.appointmentService.patch(id, appointmentCancellation));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> delete(
			@PathVariable final Long id
	) {
		this.appointmentService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
