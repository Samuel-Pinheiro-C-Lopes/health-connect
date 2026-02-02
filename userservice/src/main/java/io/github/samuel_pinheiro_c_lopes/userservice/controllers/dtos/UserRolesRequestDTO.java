package io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos;

import java.util.List;

public record UserRolesRequestDTO(List<Long> roles) { }
