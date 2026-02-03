package io.github.samuel_pinheiro_c_lopes.userservice.dtos.user;

import java.util.List;

public record UserRolesRequestDTO(List<String> roles) { }
