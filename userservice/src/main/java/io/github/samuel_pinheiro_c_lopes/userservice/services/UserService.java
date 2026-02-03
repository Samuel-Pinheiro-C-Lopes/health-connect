package io.github.samuel_pinheiro_c_lopes.userservice.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.samuel_pinheiro_c_lopes.userservice.dtos.user.UserRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.dtos.user.UserResponseDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.dtos.user.UserRolesRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.models.Role;
import io.github.samuel_pinheiro_c_lopes.userservice.models.User;
import io.github.samuel_pinheiro_c_lopes.userservice.repositories.PersonRepository;
import io.github.samuel_pinheiro_c_lopes.userservice.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;

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
	
	public UserResponseDTO findById(final Long id) {
		return this.userRepository
				.findById(id)
				.map(UserResponseDTO::new)
				.orElseThrow(() -> new EntityNotFoundException());
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

	@Transactional
	public UserResponseDTO grantRoles(final Long userId, UserRolesRequestDTO userRolesRequest) {
	    final User user = this.userRepository.findById(userId)
	            .orElseThrow(() -> new EntityNotFoundException("User not found"));

	    List<Role> newRoles = userRolesRequest.roles().stream()
	            .map(roleId -> new Role(roleId))
	            .toList();
	 
	    for (Role newRole : newRoles) 
	        if (!user.getRoles().contains(newRole)) 
	            user.getRoles().add(newRole);
	        
	    return new UserResponseDTO(this.userRepository.save(user));
	}
}
