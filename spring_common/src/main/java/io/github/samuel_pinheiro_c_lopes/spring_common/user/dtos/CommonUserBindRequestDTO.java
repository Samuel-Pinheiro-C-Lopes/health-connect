package io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonUserBindRequestDTO {

    @JsonProperty("patientId")
    private Long patientId;

    @JsonProperty("doctorId")
    private Long doctorId;

    public CommonUserBindRequestDTO() {
    }

    // Constructor for convenience
    public CommonUserBindRequestDTO(Long patientId, Long doctorId) {
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    // Interface method match
    public Long patientId() { return patientId; }
    public Long doctorId() { return doctorId; }

    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
}