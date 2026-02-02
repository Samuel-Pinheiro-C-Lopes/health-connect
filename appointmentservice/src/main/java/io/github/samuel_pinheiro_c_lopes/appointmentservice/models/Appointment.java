package io.github.samuel_pinheiro_c_lopes.appointmentservice.models;
import java.time.LocalDateTime;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.models.enums.AppointmentStatus;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.models.enums.CancelReason;
import jakarta.persistence.*;
@Entity
@Table(
    name = "appointments",
    indexes = {
        @Index(name = "idx_appointments_patient_day", columnList = "patient_id,date_time"),
        @Index(name = "idx_appointments_doctor_time", columnList = "doctor_id,date_time")
    }
)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="date_time", nullable = false)
    private LocalDateTime dateTime;
    @Column(name="end_time", nullable = false)
    private LocalDateTime endTime;
    @Column(name = "patient_id", nullable = false)
    private Long patientId;
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;
    @Enumerated(EnumType.STRING)
    @Column(name = "cancel_reason")
    private CancelReason cancelReason;
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;
    public Appointment() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public CancelReason getCancelReason() { return cancelReason; }
    public void setCancelReason(CancelReason cancelReason) { this.cancelReason = cancelReason; }
    public LocalDateTime getCanceledAt() { return canceledAt; }
    public void setCanceledAt(LocalDateTime canceledAt) { this.canceledAt = canceledAt; }
}