package io.github.samuel_pinheiro_c_lopes.spring_common.patient.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.samuel_pinheiro_c_lopes.spring_common.patient.dtos.CommonPatientResponseDTO;

@FeignClient(name = "patientservice", path = "/patient")
public interface PatientClient {
	@GetMapping("/{id}")
	public CommonPatientResponseDTO findById(@PathVariable("id") final Long id);
}
