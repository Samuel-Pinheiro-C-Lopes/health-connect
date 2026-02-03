package io.github.samuel_pinheiro_c_lopes.spring_common.person.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.samuel_pinheiro_c_lopes.spring_common.person.dtos.CommonPersonBindPatchDTO;
import io.github.samuel_pinheiro_c_lopes.spring_common.person.dtos.CommonPersonResponseDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "userservice", path = "/person")
public interface PersonClient {
	@GetMapping("/loggedIn")
	public CommonPersonResponseDTO findCurrentlyLoggedIn();
	
	@GetMapping("/{id}")
	public CommonPersonResponseDTO findById(@PathVariable("id") final Long id);
	
	@GetMapping("/doctor/{doctorId}")
	public CommonPersonResponseDTO findByDoctorId(@PathVariable("doctorId") final Long doctorId);
	
	@GetMapping("/patient/{patientId}")
	public CommonPersonResponseDTO findByPatientId(@PathVariable("patientId") final Long patientId);
	
	@PatchMapping("/{id}")
	public CommonPersonResponseDTO patch(
		@PathVariable("id") final Long id, 
		@RequestBody final CommonPersonBindPatchDTO personBindRequest
	);
}
