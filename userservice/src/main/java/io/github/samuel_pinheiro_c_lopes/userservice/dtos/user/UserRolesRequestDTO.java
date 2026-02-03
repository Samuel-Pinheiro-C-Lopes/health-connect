package io.github.samuel_pinheiro_c_lopes.userservice.dtos.user;

import java.util.List;

import io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos.CommonUserRolesRequestDTO;

public record UserRolesRequestDTO(List<String> roles) implements CommonUserRolesRequestDTO { }
