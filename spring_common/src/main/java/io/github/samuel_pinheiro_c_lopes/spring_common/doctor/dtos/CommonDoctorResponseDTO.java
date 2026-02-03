package io.github.samuel_pinheiro_c_lopes.spring_common.doctor.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.samuel_pinheiro_c_lopes.spring_common.doctor.enums.Specialty;

public class CommonDoctorResponseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("personId")
    private Long personId;

    @JsonProperty("crm")
    private String crm;

    @JsonProperty("specialty")
    private Specialty specialty;

    public CommonDoctorResponseDTO() {
    }

    public Long id() { return id; }
    public Long personId() { return personId; }
    public String crm() { return crm; }
    public Specialty specialty() { return specialty; }

    public void setId(Long id) { this.id = id; }
    public void setPersonId(Long personId) { this.personId = personId; }
    public void setCrm(String crm) { this.crm = crm; }
    public void setSpecialty(Specialty specialty) { this.specialty = specialty; }
}