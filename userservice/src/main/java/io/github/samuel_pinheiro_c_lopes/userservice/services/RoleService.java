package io.github.samuel_pinheiro_c_lopes.userservice.services;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos.RoleRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos.RoleResponseDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.models.Role;
import io.github.samuel_pinheiro_c_lopes.userservice.repositories.RoleRepository;

@Service
public class RoleService {
	private final RoleRepository roleRepository;
	private final Set<String> availableRoles;

	@Autowired
	public RoleService(
			final RoleRepository roleRepository,
	        @Value("${app.security.roles.admin}") final String adminRole,
	        @Value("${app.security.roles.doctor}") final String doctorRole,
	        @Value("${app.security.roles.patient}") final String patientRole,
	        @Value("${app.security.roles.manager}") final String managerRole
	) {
		this.roleRepository = roleRepository;
		this.availableRoles = Set.of(adminRole, doctorRole, patientRole, managerRole);
	}
	
	public List<RoleResponseDTO> findAll() {
		return this.roleRepository.findAll().stream().map(RoleResponseDTO::new).toList();
	}
	
	public Role save(final RoleRequestDTO roleRequest) {
		return this.roleRepository.save(new Role(roleRequest.name()));
	}
	
	public List<Role> save(final RoleRequestDTO... roles) {
		return this.roleRepository.saveAll(Arrays
				.asList(roles)
				.stream()
				.map(r -> new Role(r.name()))
				.toList()
		);
	}
	
	public List<Role> saveAll(final List<RoleRequestDTO> roles) {
		return this.roleRepository.saveAll(roles
				.stream()
				.map(r -> new Role(r.name()))
				.toList()
		);
	}
	
	public void delete(final Long roleId) {
		this.roleRepository.deleteById(roleId);
	}
	
	public Set<String> getAvailableRoles() {
		return this.availableRoles;
	}
}
