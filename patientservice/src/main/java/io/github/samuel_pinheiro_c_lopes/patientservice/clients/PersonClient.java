package io.github.samuel_pinheiro_c_lopes.patientservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.samuel_pinheiro_c_lopes.spring_common.person.dtos.CommonPersonResponseDTO;

@FeignClient(name = "personservice", path = "/person")
public interface PersonClient {
	@GetMapping("/{id}")
	public CommonPersonResponseDTO findById(@PathVariable final Long id);
	
	@GetMapping("/doctor/{doctorId}")
	public CommonPersonResponseDTO findByDoctorId(@PathVariable final Long doctorId);
	
	@GetMapping("/patient/{patientId}")
	public CommonPersonResponseDTO findByPatientId(@PathVariable final Long patientId);
}
