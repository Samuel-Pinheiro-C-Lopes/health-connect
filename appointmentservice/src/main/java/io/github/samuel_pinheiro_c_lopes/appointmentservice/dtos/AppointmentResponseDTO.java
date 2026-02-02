package io.github.samuel_pinheiro_c_lopes.appointmentservice.dtos;
import java.time.LocalDateTime;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.models.Appointment;

public record AppointmentResponseDTO(
        Long id,
        LocalDateTime dateTime,
        Long patientId,
        Long doctorId,
        String status,
        String cancelReason,
        LocalDateTime canceledAt
) {
    public AppointmentResponseDTO(final Appointment appointment) {
        this(
            appointment.getId(),
            appointment.getDateTime(),
            appointment.getPatientId(),
            appointment.getDoctorId(),
            appointment.getStatus() == null ? null : appointment.getStatus().name(),
            appointment.getCancelReason() == null ? null : appointment.getCancelReason().name(),
            appointment.getCanceledAt()
        );
    }
}