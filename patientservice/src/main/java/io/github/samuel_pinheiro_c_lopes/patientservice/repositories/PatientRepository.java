package io.github.samuel_pinheiro_c_lopes.patientservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.samuel_pinheiro_c_lopes.patientservice.models.Patient;
import io.github.samuel_pinheiro_c_lopes.spring_common.general.enums.AccountStatus;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>{
	List<Patient> findAllByAccountStatus(AccountStatus accountStatus);
}
