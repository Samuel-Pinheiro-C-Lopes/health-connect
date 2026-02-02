package io.github.samuel_pinheiro_c_lopes.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.samuel_pinheiro_c_lopes.userservice.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> { }
