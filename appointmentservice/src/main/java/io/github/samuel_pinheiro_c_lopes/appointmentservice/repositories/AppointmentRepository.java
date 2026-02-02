package io.github.samuel_pinheiro_c_lopes.appointmentservice.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.samuel_pinheiro_c_lopes.appointmentservice.models.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
	List<Appointment> findByPatientId(Long patientId);
	List<Appointment> findByDoctorId(Long doctorId);
	Boolean existsByByDateTimeBetweenAndPatientId(LocalDateTime startOfDay, LocalDateTime endOfDay, Long patientId);
	Boolean existsByByDateTimeBetweenAndDoctorId(LocalDateTime startOfDay, LocalDateTime endOfDay, Long doctorId);
}
