package io.github.samuel_pinheiro_c_lopes.spring_common.patient.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonPatientResponseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("personId")
    private Long personId;

    @JsonProperty("cpf")
    private String cpf;

    public CommonPatientResponseDTO() {
    }

    // Interface method match
    public Long id() { return id; }
    public Long personId() { return personId; }
    public String cpf() { return cpf; }

    public void setId(Long id) { this.id = id; }
    public void setPersonId(Long personId) { this.personId = personId; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}