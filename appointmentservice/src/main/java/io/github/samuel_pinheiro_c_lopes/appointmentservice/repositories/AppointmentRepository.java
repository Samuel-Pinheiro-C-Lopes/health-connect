package io.github.samuel_pinheiro_c_lopes.appointmentservice.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.samuel_pinheiro_c_lopes.appointmentservice.models.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
    Boolean existsByDateTimeBetweenAndPatientId(LocalDateTime start, LocalDateTime end, Long patientId);
    Boolean existsByDateTimeBetweenAndPatientIdAndIdNot(LocalDateTime start, LocalDateTime end, Long patientId, Long idToExclude);
    Boolean existsByDateTimeBetweenAndDoctorId(LocalDateTime startOfDay, LocalDateTime endOfDay, Long doctorId);
    @Query("SELECT a.doctorId FROM Appointment a WHERE a.dateTime = :dateTime")
    List<Long> findBusyDoctorIds(@Param("dateTime") LocalDateTime dateTime);
    List<Appointment> findByPatientId(Long patientId);
	List<Appointment> findByDoctorId(Long doctorId);
	List<Appointment> findAllByDateTimeBetweenAndPatientId(LocalDateTime startOfDay, LocalDateTime endOfDay, Long patientId);
	

}
