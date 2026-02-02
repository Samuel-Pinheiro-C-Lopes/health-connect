package io.github.samuel_pinheiro_c_lopes.userservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.samuel_pinheiro_c_lopes.userservice.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	Optional<Person> findByPatientId(Long patientId);
	Optional<Person> findByDoctorId(Long doctorId);
}
