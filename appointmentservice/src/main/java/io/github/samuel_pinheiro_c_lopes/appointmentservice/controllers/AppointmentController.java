package io.github.samuel_pinheiro_c_lopes.appointmentservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.samuel_pinheiro_c_lopes.appointmentservice.controllers.dtos.AppointmentRequestDTO;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.controllers.dtos.AppointmentResponseDTO;
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
	@PreAuthorize("hasAuthority(@rolesConfiguration.admin) or hasAuthority(@rolesConfiguration.manager) or hasAuthority(@rolesConfiguration.doctor)")
	public ResponseEntity<List<AppointmentResponseDTO>> findAll() {
		return ResponseEntity.ok(this.appointmentService.findAll());
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority(@rolesConfiguration.admin) or hasAuthority(@rolesConfiguration.manager) or hasAuthority(@rolesConfiguration.doctor")
	public ResponseEntity<AppointmentResponseDTO> save(
			@RequestBody final AppointmentRequestDTO userRequest
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.appointmentService.save(userRequest));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority(@rolesConfiguration.admin) or hasAuthority(@rolesConfiguration.manager) or hasAuthority(@rolesConfiguration.doctor")
	public ResponseEntity<AppointmentResponseDTO> delete(
			@PathVariable final Long id, 
			@RequestBody final AppointmentRequestDTO userRequest
	) {
		return ResponseEntity.ok(this.appointmentService.update(id, userRequest));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority(@rolesConfiguration.admin) or hasAuthority(@rolesConfiguration.manager) or hasAuthority(@rolesConfiguration.doctor")
	public ResponseEntity<Void> delete(
			@PathVariable final Long id
	) {
		this.appointmentService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
