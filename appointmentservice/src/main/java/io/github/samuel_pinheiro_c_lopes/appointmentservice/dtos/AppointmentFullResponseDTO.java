package io.github.samuel_pinheiro_c_lopes.appointmentservice.dtos;

import java.time.LocalDateTime;

import io.github.samuel_pinheiro_c_lopes.spring_common.appointment.enums.AppointmentStatus;
import io.github.samuel_pinheiro_c_lopes.spring_common.doctor.enums.Specialty;

public record AppointmentFullResponseDTO(
        Long id,
        LocalDateTime dateTime,
        AppointmentStatus status,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        String doctorCrm,
        Specialty doctorSpecialty
) { }
