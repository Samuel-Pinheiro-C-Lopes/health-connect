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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos.UserRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos.UserResponseDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.services.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {
	private final UserService userService;
	
	@Autowired
	public UserController(final UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
	public ResponseEntity<List<UserResponseDTO>> findAll() {
		return ResponseEntity.ok(this.userService.findAll());
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
	public ResponseEntity<UserResponseDTO> save(
			@RequestBody final UserRequestDTO userRequest
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.save(userRequest));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
	public ResponseEntity<UserResponseDTO> delete(
			@PathVariable final Long id, 
			@RequestBody final UserRequestDTO userRequest
	) {
		return ResponseEntity.ok(this.userService.update(id, userRequest));
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
