package io.github.samuel_pinheiro_c_lopes.spring_common.patient.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.samuel_pinheiro_c_lopes.spring_common.patient.dtos.CommonPatientResponseDTO;

@FeignClient(name = "patientservice", path = "/person")
public interface PatientClient {
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority(@rolesConfiguration.admin) or hasAuthority(@rolesConfiguration.manager) or hasAuthority(@rolesConfiguration.doctor)")
	public CommonPatientResponseDTO findById(@PathVariable("id") final Long id);
	@GetMapping("/active")
	public ResponseEntity<List<CommonPatientResponseDTO>> findAllActive();
}
