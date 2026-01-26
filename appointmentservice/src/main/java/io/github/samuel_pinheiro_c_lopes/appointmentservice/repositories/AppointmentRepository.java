package io.github.samuel_pinheiro_c_lopes.appointmentservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.samuel_pinheiro_c_lopes.appointmentservice.models.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{ }
