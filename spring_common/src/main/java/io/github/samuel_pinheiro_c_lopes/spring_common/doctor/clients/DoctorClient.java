package io.github.samuel_pinheiro_c_lopes.spring_common.doctor.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.samuel_pinheiro_c_lopes.spring_common.doctor.dtos.CommonDoctorResponseDTO;
import io.github.samuel_pinheiro_c_lopes.spring_common.patient.dtos.CommonPatientResponseDTO;

@FeignClient(name = "doctorservice", path = "/doctor")
public interface DoctorClient {
    @GetMapping("/{id}")
    public CommonDoctorResponseDTO findById(@PathVariable("id") final Long id);
    @GetMapping
    public List<CommonDoctorResponseDTO> findAll();
    @GetMapping("/active")
	public List<CommonDoctorResponseDTO> findAllActive();
}
