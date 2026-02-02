package io.github.samuel_pinheiro_c_lopes.userservice.configuration.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.github.samuel_pinheiro_c_lopes.userservice.dtos.role.RoleRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.dtos.user.UserRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.dtos.user.UserResponseDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.dtos.user.UserRolesRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.models.Role;
import io.github.samuel_pinheiro_c_lopes.userservice.services.RoleService;
import io.github.samuel_pinheiro_c_lopes.userservice.services.UserService;

@Component
public class AdminInitializer implements CommandLineRunner {
	@Value("${app.security.users.admin.username}")
	private String adminUsername;
	@Value("${app.security.users.admin.password}")
	private String adminPassword;
	@Value("${app.security.roles.admin}")
	private String adminRole;
	
	private final UserService userService;
	private final RoleService roleService;
	
	@Autowired
	public AdminInitializer(final UserService userService, final RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}
	
	@Override
	public void run(String... args) throws Exception {
		final List<Role> roles = this.saveRoles();
		
		final Role adminRole = roles
				.stream()
				.filter(r -> this.adminRole.equals(r.getAuthority()))
				.findFirst()
				.orElseThrow(() -> new AdminRoleNotFoundException());;
				
		this.saveAdminUser(adminRole);
	}
	
	private List<Role> saveRoles() {
		return this.roleService.saveAll(
			this.roleService.getAvailableRoles()
			.stream()
			.map(r -> new RoleRequestDTO(r))
			.toList()
		);
	}
	
	private void saveAdminUser(final Role adminRole) {
		final UserResponseDTO admin = this.userService.save(new UserRequestDTO(
				this.adminUsername,
				this.adminPassword
		));
		this.userService.grantRoles(admin.id(), new UserRolesRequestDTO(List.of(adminRole.getId())));
	}
	
	private class AdminRoleNotFoundException extends Exception {
		private static final long serialVersionUID = 1L;
	}
}
