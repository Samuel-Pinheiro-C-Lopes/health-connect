package io.github.samuel_pinheiro_c_lopes.doctorservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.samuel_pinheiro_c_lopes.doctorservice.models.Doctor;
import io.github.samuel_pinheiro_c_lopes.spring_common.general.enums.AccountStatus;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	List<Doctor> findAllByAccountStatus(AccountStatus accountStatus);
}
