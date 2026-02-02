package io.github.samuel_pinheiro_c_lopes.userservice.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos.RoleResponseDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos.UserRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos.UserResponseDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos.UserRolesRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.models.Role;
import io.github.samuel_pinheiro_c_lopes.userservice.models.User;
import io.github.samuel_pinheiro_c_lopes.userservice.repositories.PersonRepository;
import io.github.samuel_pinheiro_c_lopes.userservice.repositories.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PersonRepository personRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(final UserRepository userRepository, final PersonRepository personRepository, final PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.personRepository = personRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public UserResponseDTO save(final UserRequestDTO userRequest) {
		final User user = userRequest.toUser();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return new UserResponseDTO(this.userRepository.save(user));
	}
	
	public List<UserResponseDTO> findAll() {
		return this.userRepository.findAll()
				.stream()
				.map(u -> new UserResponseDTO(u))
				.toList();
	}
	
	public UserResponseDTO update(final Long id, final UserRequestDTO userRequest) {
		final User toBeUpdatedUser = this.userRepository.getReferenceById(id);
		
		toBeUpdatedUser.setEmail(userRequest.email());
		toBeUpdatedUser.setPassword(userRequest.password());
		toBeUpdatedUser.setPerson(personRepository.getReferenceById(id));
		
		return new UserResponseDTO(this.userRepository.save(toBeUpdatedUser));
	}
	
	public void delete(final Long id) {
		this.userRepository.delete(this.userRepository.getReferenceById(id));
	}

	public UserResponseDTO grantRoles(final Long userId, UserRolesRequestDTO userRolesRequest) {
		final User toBeGrantedRolesUser = this.userRepository.getReferenceById(userId);
		

		final Set<Long> roles = toBeGrantedRolesUser.getRoles()
				.stream()
				.map(r -> r.getId())
				.collect(Collectors.toSet());
		
		roles.addAll(userRolesRequest.roles());
		
		toBeGrantedRolesUser.setRoles(roles.stream().map(r -> new Role(r)).toList());
		
		return new UserResponseDTO(this.userRepository.save(toBeGrantedRolesUser));
	}
}
