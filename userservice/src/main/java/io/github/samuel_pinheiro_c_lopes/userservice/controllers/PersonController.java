package io.github.samuel_pinheiro_c_lopes.userservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.samuel_pinheiro_c_lopes.userservice.dtos.person.PersonBindPatchDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.dtos.person.PersonRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.dtos.person.PersonResponseDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.services.PersonService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

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
		
		@GetMapping("/")
		@PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
		public ResponseEntity<List<PersonResponseDTO>> findAllByDoctorIds() {
			return ResponseEntity.ok(this.personService.findAll());
		}
		
		@GetMapping("")
		@PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
		public ResponseEntity<List<PersonResponseDTO>> findAllByPatientIds() {
			return ResponseEntity.ok(this.personService.findAll());
		}
		
		@GetMapping("/{id}")
		@PreAuthorize("isAuthenticated()")
		public ResponseEntity<PersonResponseDTO> findById(@PathVariable final Long id) {
			return ResponseEntity.ok(this.personService.findById(id));
		}
		
		@GetMapping("/email/{email}")
		@PreAuthorize("isAuthenticated()")
		public ResponseEntity<PersonResponseDTO> findByEmail(@PathVariable final String email) {
			return ResponseEntity.ok(this.personService.findByEmail(email));
		}
		
		@GetMapping("/doctor/{doctorId}")
		@PreAuthorize("isAuthenticated()")
		public ResponseEntity<PersonResponseDTO> findByDoctorId(@PathVariable final Long doctorId) {
			return ResponseEntity.ok(this.personService.findByDoctorId(doctorId));
		}
		
		@GetMapping("/patient/{patientId}")
		@PreAuthorize("isAuthenticated()")
		public ResponseEntity<PersonResponseDTO> findByPatientId(@PathVariable final Long patientId) {
			return ResponseEntity.ok(this.personService.findByPatientId(patientId));
		}
		
		@GetMapping("/loggedIn")
		@PreAuthorize("isAuthenticated()")
		public ResponseEntity<PersonResponseDTO> findCurrentlyLoggedIn() {
			return ResponseEntity.ok(this.personService.findCurrentlyLoggedIn());
		}
		
		@PostMapping("/loggedIn")
		@PreAuthorize("isAuthenticated()")
		public ResponseEntity<PersonResponseDTO> saveCurrentlyLoggedIn(
				@RequestBody final PersonRequestDTO personRequest
		) {
			return ResponseEntity.status(HttpStatus.CREATED).body(this.personService.saveCurrentlyLoggedIn(personRequest));
		}
		
		@PostMapping
		@PreAuthorize("isAuthenticated()")
		public ResponseEntity<PersonResponseDTO> save(
			@RequestBody final PersonRequestDTO personRequest
		){
			return ResponseEntity.status(HttpStatus.CREATED).body(this.personService.save(personRequest));
		}
		
		@PutMapping("/{id}")
		@PreAuthorize("isAuthenticated()")
		public ResponseEntity<PersonResponseDTO> update(
				@PathVariable final Long id, 
				@RequestBody final PersonRequestDTO userRequest
		) {
			return ResponseEntity.ok(this.personService.update(id, userRequest));
		}
		
		@PatchMapping("/{id}")
		@PreAuthorize("isAuthenticated()")
		public ResponseEntity<PersonResponseDTO> patch(
			@PathVariable final Long id,	
			@RequestBody final PersonBindPatchDTO personBindRequest
		) {
			return ResponseEntity.ok(this.personService.bindPerson(id, personBindRequest));
		}
		
		/*
		@DeleteMapping("/{id}")
		@PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
		public ResponseEntity<Void> delete(
				@PathVariable final Long id
		) {
			this.personService.delete(id);
			return ResponseEntity.noContent().build();
		}
		*/
	}

