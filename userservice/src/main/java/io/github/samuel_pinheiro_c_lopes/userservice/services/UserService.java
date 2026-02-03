package io.github.samuel_pinheiro_c_lopes.userservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.samuel_pinheiro_c_lopes.userservice.dtos.user.UserBindRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.dtos.user.UserRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.dtos.user.UserResponseDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.dtos.user.UserRolesRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.models.Address;
import io.github.samuel_pinheiro_c_lopes.userservice.models.Role;
import io.github.samuel_pinheiro_c_lopes.userservice.models.User;
import io.github.samuel_pinheiro_c_lopes.userservice.repositories.RoleRepository;
import io.github.samuel_pinheiro_c_lopes.userservice.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final String adminRole;
	private final String doctorRole;
	private final String patientRole;

	@Autowired
	public UserService(
			final UserRepository userRepository, 
			final PasswordEncoder passwordEncoder,
			final RoleRepository roleRepository,
			@Value("${app.security.roles.admin}") final String adminRole,
	        @Value("${app.security.roles.doctor}") final String doctorRole,
	        @Value("${app.security.roles.patient}") final String patientRole) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.adminRole = adminRole;
		this.patientRole = patientRole;
		this.doctorRole = doctorRole;
	}
	
	public List<UserResponseDTO> findAll() {
		return this.userRepository.findAll()
				.stream()
				.map(UserResponseDTO::new)
				.toList();
	}
	
	public UserResponseDTO findById(final Long id) {
		return this.userRepository
				.findById(id)
				.map(UserResponseDTO::new)
				.orElseThrow(() -> new EntityNotFoundException());
	}
	
	public UserResponseDTO findByEmail(final String email) {
		return this.userRepository
				.findUserByEmail(email)
				.map(UserResponseDTO::new)
				.orElseThrow(() -> new EntityNotFoundException());
	}
	
	public UserResponseDTO findByPatientId(final Long id) {
		return new UserResponseDTO(this.userRepository.findByPatientId(id).orElseThrow(() -> new EntityNotFoundException()));
	}
	
	public UserResponseDTO findByDoctorId(final Long id) {
		return new UserResponseDTO(this.userRepository.findByDoctorId(id).orElseThrow(() -> new EntityNotFoundException()));
	}
	
	
	public UserResponseDTO save(final UserRequestDTO userRequest) {
		final User user = userRequest.toUser();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return new UserResponseDTO(this.userRepository.save(user));
	}
	
	public UserResponseDTO findCurrentlyLoggedIn() {
		// finds authenticated user
		final String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		final User user = this.userRepository.findUserByEmail(userEmail).orElseThrow(() -> new EntityNotFoundException());
		
		// returns it
		return new UserResponseDTO(user);
	}
	
	public UserResponseDTO update(final Long id, final UserRequestDTO userRequest) {
		final User toBeUpdatedUser = this.userRepository
				.findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		
		final Address newAddress = new Address(
				userRequest.postalCode(),
				userRequest.avenue(),
				userRequest.complement(),
				userRequest.number(),
				userRequest.city(),
				userRequest.district(),
				userRequest.state()
		);
		
		toBeUpdatedUser.setEmail(userRequest.email());
		toBeUpdatedUser.setPassword(userRequest.password());
		toBeUpdatedUser.setName(userRequest.name());
		toBeUpdatedUser.setPhone(userRequest.phone());
		toBeUpdatedUser.setAddress(newAddress);
		
		return new UserResponseDTO(this.userRepository.save(toBeUpdatedUser));
	}
	
	@Transactional
    public UserResponseDTO patch(final Long id, final UserBindRequestDTO userBind) {
        final User toBeUpdatedUser = this.userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        if (userBind.doctorId() != null) {
            toBeUpdatedUser.setDoctorId(userBind.doctorId());
            this.addRolesInternal(toBeUpdatedUser, List.of(this.doctorRole));
        }
        
        if (userBind.patientId() != null) {
            toBeUpdatedUser.setPatientId(userBind.patientId());
            this.addRolesInternal(toBeUpdatedUser, List.of(this.patientRole));
        }
        
        return new UserResponseDTO(this.userRepository.save(toBeUpdatedUser));
    }

    @Transactional
    public UserResponseDTO grantRoles(final Long userId, UserRolesRequestDTO userRolesRequest) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        this.addRolesInternal(user, userRolesRequest.roles());
            
        return new UserResponseDTO(this.userRepository.save(user));
    }

    // Private helper method to handle the logic of adding unique roles.  
    private void addRolesInternal(User user, List<String> rolesToAdd) {
        if (rolesToAdd == null || rolesToAdd.isEmpty()) return;

        List<Role> newRoles = rolesToAdd.stream()
                .map(r -> this.roleRepository.findByAuthority(r)
                        .orElseThrow(() -> new EntityNotFoundException("Role not found: " + r)))
                // Filter out roles the user already has
                .filter(r -> !user.getRoles().contains(r)) 
                .toList();
        
        user.getRoles().addAll(newRoles);
    }
	
	public void delete(final Long id) {
		this.userRepository.delete(this.userRepository.getReferenceById(id));
	}
}
