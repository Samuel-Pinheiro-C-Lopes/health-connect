package io.github.samuel_pinheiro_c_lopes.userservice.controllers;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.samuel_pinheiro_c_lopes.userservice.dtos.user.UserRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.dtos.user.UserResponseDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.dtos.user.UserRolesRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	private final UserService userService;
	
	@Autowired
	public UserController(final UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<UserResponseDTO>> findAll() {
		return ResponseEntity.ok(this.userService.findAll());
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserResponseDTO> findById(@PathVariable final Long id) {
		return ResponseEntity.ok(this.userService.findById(id));
	}
	
	@GetMapping("/email/{email}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable final String email) {
		return ResponseEntity.ok(this.userService.findByEmail(email));
	}
	
	@GetMapping("/doctor/{doctorId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserResponseDTO> findByDoctorId(@PathVariable final Long doctorId) {
		return ResponseEntity.ok(this.userService.findByDoctorId(doctorId));
	}
	
	@GetMapping("/patient/{patientId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserResponseDTO> findByPatientId(@PathVariable final Long patientId) {
		return ResponseEntity.ok(this.userService.findByPatientId(patientId));
	}
	
	@GetMapping("/loggedIn")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserResponseDTO> findCurrentlyLoggedIn() {
		return ResponseEntity.ok(this.userService.findCurrentlyLoggedIn());
	}
	
	@PostMapping
	public ResponseEntity<UserResponseDTO> save(
			@Valid @RequestBody final UserRequestDTO userRequest
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.save(userRequest));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserResponseDTO> update(
			@PathVariable final Long id, 
			@Valid @RequestBody final UserRequestDTO userRequest
	) {
		return ResponseEntity.ok(this.userService.update(id, userRequest));
	}
	
	@PutMapping("/{id}/roles")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserResponseDTO> grantRoles(
			@PathVariable final Long id,
			@RequestBody final UserRolesRequestDTO userRolesRequest
	) {
		return ResponseEntity.ok(this.userService.grantRoles(id, userRolesRequest));
		
	}
	
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
	public ResponseEntity<Void> delete(
			@PathVariable final Long id
	) {
		this.userService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
