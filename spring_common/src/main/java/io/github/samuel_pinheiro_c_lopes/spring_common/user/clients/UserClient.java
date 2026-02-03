package io.github.samuel_pinheiro_c_lopes.spring_common.user.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos.CommonUserBindRequestDTO;
import io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos.CommonUserResponseDTO;
import io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos.CommonUserRolesRequestDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "userservice", path = "/user")
public interface UserClient {
	@GetMapping("/loggedIn")
	public CommonUserResponseDTO findCurrentlyLoggedIn();
	
	@GetMapping("/{id}")
	public CommonUserResponseDTO findById(@PathVariable("id") final Long id);
	
	@GetMapping("/doctor/{doctorId}")
	public CommonUserResponseDTO findByDoctorId(@PathVariable("doctorId") final Long doctorId);
	
	@GetMapping("/patient/{patientId}")
	public CommonUserResponseDTO findByPatientId(@PathVariable("patientId") final Long patientId);
	
	@GetMapping("/email/{email}")
	public CommonUserResponseDTO findByEmail(@PathVariable("email") final String email);
	
	@PatchMapping("/{id}")
	public CommonUserResponseDTO patch(
		@PathVariable("id") final Long id, 
		@RequestBody final CommonUserBindRequestDTO personBindRequest
	);
	
	@PutMapping("/{id}/roles")
	public CommonUserResponseDTO grantRoles(
			@PathVariable("id") final Long id,
			@RequestBody final CommonUserRolesRequestDTO userRolesRequest
	);
}
