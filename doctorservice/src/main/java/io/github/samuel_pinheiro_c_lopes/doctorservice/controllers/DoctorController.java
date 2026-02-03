package io.github.samuel_pinheiro_c_lopes.doctorservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.samuel_pinheiro_c_lopes.doctorservice.dtos.DoctorFullResponseDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.dtos.DoctorRequestDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.dtos.DoctorResponseDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.services.DoctorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/doctor")
@Validated
public class DoctorController {
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(final DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
    public ResponseEntity<List<DoctorResponseDTO>> findAll() {
        return ResponseEntity.ok(this.doctorService.findAll());
    }

    @GetMapping("/full")
    @PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
    public ResponseEntity<List<DoctorFullResponseDTO>> findAllFull() {
        return ResponseEntity.ok(this.doctorService.findAllFull());
    }

    @GetMapping("/active/full")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DoctorFullResponseDTO>> findAllActiveFull() {
        return ResponseEntity.ok(this.doctorService.findAllActiveFull());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
    public ResponseEntity<List<DoctorFullResponseDTO>> findAllPending() {
        return ResponseEntity.ok(this.doctorService.findAllPending());
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
    public ResponseEntity<Void> approve(@PathVariable final Long id) {
        this.doctorService.approve(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
    public ResponseEntity<Void> reject(@PathVariable final Long id) {
        this.doctorService.reject(id);
        return ResponseEntity.noContent().build();
    }
  
    
	@GetMapping("/active")
	@PreAuthorize("isAuthenticated()") 
	public ResponseEntity<List<DoctorResponseDTO>> findAllActive() {
		return ResponseEntity.ok(this.doctorService.findAllActive());
	}

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()") 
    public ResponseEntity<DoctorResponseDTO> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(this.doctorService.findById(id));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()") 
    public ResponseEntity<DoctorResponseDTO> save(@Valid @RequestBody final DoctorRequestDTO doctorRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.doctorService.save(doctorRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(@rolesConfiguration.admin, @rolesConfiguration.doctor)")
    public ResponseEntity<DoctorResponseDTO> update(
            @PathVariable final Long id,
            @Valid @RequestBody final DoctorRequestDTO doctorRequest
    ) {
        return ResponseEntity.ok(this.doctorService.update(id, doctorRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(@rolesConfiguration.doctor, @rolesConfiguration.admin) and (!#permanent or hasAuthority(@rolesConfiguration.admin))")
    public ResponseEntity<Void> delete(
			@PathVariable final Long id,
			@RequestParam(defaultValue = "false") boolean permanent
	) {
    	if (permanent) this.doctorService.delete(id);
    	else this.doctorService.deactivate(id);
    	
        return ResponseEntity.noContent().build();
    }
}
