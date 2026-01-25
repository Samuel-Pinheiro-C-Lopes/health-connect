package io.github.samuel_pinheiro_c_lopes.patientservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.samuel_pinheiro_c_lopes.patientservice.models.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>{

}
