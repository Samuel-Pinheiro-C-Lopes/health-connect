package io.github.samuel_pinheiro_c_lopes.userservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.samuel_pinheiro_c_lopes.userservice.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	@EntityGraph(attributePaths = {"roles"})
	Optional<User> findUserByEmailIgnoreCase(String email);
	Optional<User> findByPatientId(Long patientId);
	Optional<User> findByDoctorId(Long doctorId);
}
