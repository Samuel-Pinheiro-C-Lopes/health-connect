package io.github.samuel_pinheiro_c_lopes.doctorservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.samuel_pinheiro_c_lopes.doctorservice.models.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
