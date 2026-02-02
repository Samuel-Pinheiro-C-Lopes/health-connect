package io.github.samuel_pinheiro_c_lopes.patientservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.samuel_pinheiro_c_lopes.patientservice.clients.dtos.PersonResponseDTO;

@FeignClient(name = "userservice", url = "${app.clients.userservice.url}")
public interface UserserviceClient {

	@GetMapping("/person/{id}")
	PersonResponseDTO findPersonById(@PathVariable("id") Long id);
}
