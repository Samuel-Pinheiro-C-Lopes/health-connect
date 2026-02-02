package io.github.samuel_pinheiro_c_lopes.userservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos.RoleResponseDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.services.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {
	private final RoleService roleService;
	
	@Autowired
	public RoleController(final RoleService roleService) {
		this.roleService = roleService;
	}
	
	
	@GetMapping
	@PreAuthorize("hasAuthority(@rolesConfiguration.admin)")
	public ResponseEntity<List<RoleResponseDTO>> findAll() {
		return ResponseEntity.ok(this.roleService.findAll());
	}
}
