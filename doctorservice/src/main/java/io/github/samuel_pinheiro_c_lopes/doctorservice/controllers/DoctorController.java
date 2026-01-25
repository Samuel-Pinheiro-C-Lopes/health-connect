package io.github.samuel_pinheiro_c_lopes.doctorservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.samuel_pinheiro_c_lopes.doctorservice.controllers.dtos.DoctorRequestDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.controllers.dtos.DoctorResponseDTO;
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
    @PreAuthorize("hasAuthority(@rolesConfiguration.admin) or hasAuthority(@rolesConfiguration.manager)")
    public ResponseEntity<List<DoctorResponseDTO>> findAll() {
        return ResponseEntity.ok(this.doctorService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority(@rolesConfiguration.admin) or hasAuthority(@rolesConfiguration.manager) or hasAuthority(@rolesConfiguration.doctor)")
    public ResponseEntity<DoctorResponseDTO> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(this.doctorService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority(@rolesConfiguration.admin) or hasAuthority(@rolesConfiguration.manager)")
    public ResponseEntity<DoctorResponseDTO> save(@Valid @RequestBody final DoctorRequestDTO doctorRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.doctorService.save(doctorRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(@rolesConfiguration.admin) or hasAuthority(@rolesConfiguration.manager)")
    public ResponseEntity<DoctorResponseDTO> update(
            @PathVariable final Long id,
            @Valid @RequestBody final DoctorRequestDTO doctorRequest
    ) {
        return ResponseEntity.ok(this.doctorService.update(id, doctorRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(@rolesConfiguration.admin) or hasAuthority(@rolesConfiguration.manager)")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        this.doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
