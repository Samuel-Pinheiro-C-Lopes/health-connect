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

import io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos.PersonRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos.PersonResponseDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.services.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {

		private final PersonService personService;
		
		@Autowired
		public PersonController(final PersonService personService) {
			this.personService = personService;
		}
		
		@GetMapping
		@PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
		public ResponseEntity<List<PersonResponseDTO>> findAll() {
			return ResponseEntity.ok(this.personService.findAll());
		}

		// Usado internamente por outros microsserviços via OpenFeign.
		// Conforme checklist: este endpoint deve ser acessível sem autenticação.
		@GetMapping("/{id}")
		public ResponseEntity<PersonResponseDTO> findById(@PathVariable final Long id) {
			return ResponseEntity.ok(this.personService.findById(id));
		}
		
		@PostMapping
		@PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
		public ResponseEntity<PersonResponseDTO> save(
				@RequestBody final PersonRequestDTO userRequest
		) {
			return ResponseEntity.status(HttpStatus.CREATED).body(this.personService.save(userRequest));
		}
		
		@PutMapping("/{id}")
		@PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
		public ResponseEntity<PersonResponseDTO> delete(
				@PathVariable final Long id, 
				@RequestBody final PersonRequestDTO userRequest
		) {
			return ResponseEntity.ok(this.personService.update(id, userRequest));
		}
		
		@DeleteMapping("/{id}")
		@PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
		public ResponseEntity<Void> delete(
				@PathVariable final Long id
		) {
			this.personService.delete(id);
			return ResponseEntity.noContent().build();
		}
	}

