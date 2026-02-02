package io.github.samuel_pinheiro_c_lopes.appointmentservice.controllers.dtos;
import java.time.LocalDateTime;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.models.Appointment;
public record AppointmentRequestDTO(
        LocalDateTime dateTime,
        Long patientId,
        Long doctorId 
) {
    public Appointment toAppointment() {
        final Appointment a = new Appointment();
        a.setDateTime(this.dateTime);
        if (this.dateTime != null) {
            a.setEndTime(this.dateTime.plusMinutes(30));
        }
        a.setPatientId(this.patientId);
        a.setDoctorId(this.doctorId);
        return a;
    }
}