package io.github.samuel_pinheiro_c_lopes.userservice.dtos.user;

import io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos.CommonUserBindRequestDTO;

public record UserBindRequestDTO(Long patientId, Long doctorId) implements CommonUserBindRequestDTO { }
