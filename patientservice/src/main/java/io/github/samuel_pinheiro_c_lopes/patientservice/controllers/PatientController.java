package io.github.samuel_pinheiro_c_lopes.patientservice.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.samuel_pinheiro_c_lopes.patientservice.dtos.PatientFullResponseDTO;
import io.github.samuel_pinheiro_c_lopes.patientservice.dtos.PatientRequestDTO;
import io.github.samuel_pinheiro_c_lopes.patientservice.dtos.PatientResponseDTO;
import io.github.samuel_pinheiro_c_lopes.patientservice.services.PatientService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/patient")
public class PatientController {
	private final PatientService patientService;
	
	@Autowired
	public PatientController(final PatientService patientService) {
		this.patientService = patientService;
	}
	
	@GetMapping
	@PreAuthorize("hasAnyAuthority(@rolesConfiguration.admin)")
	public ResponseEntity<List<PatientFullResponseDTO>> findAll() {
		return ResponseEntity.ok(this.patientService.findAll());
	}
	
	@GetMapping("/active")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<PatientFullResponseDTO>> findAllActive() {
		return ResponseEntity.ok(this.patientService.findAllActive());
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<PatientFullResponseDTO> findById(@PathVariable final Long id) {
		return ResponseEntity.ok(this.patientService.findById(id));
	}
	
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<PatientResponseDTO> save(
			@RequestBody final PatientRequestDTO userRequest
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.patientService.save(userRequest));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<PatientResponseDTO> update(
			@PathVariable final Long id, 
			@RequestBody final PatientRequestDTO userRequest
	) {
		return ResponseEntity.ok(this.patientService.update(id, userRequest));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("isAuthenticated() and (!#permanent or hasAuthority(@rolesConfiguration.admin))")
	public ResponseEntity<Void> delete(
			@PathVariable final Long id,
			@RequestParam(defaultValue = "false") boolean permanent
	) {
		if (permanent) this.patientService.delete(id);
		else this.patientService.deactivate(id);
		
		return ResponseEntity.noContent().build();
	}
}
