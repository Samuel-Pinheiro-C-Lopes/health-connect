package io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonUserResponseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("patientId")
    private Long patientId;

    @JsonProperty("doctorId")
    private Long doctorId;

    @JsonProperty("postalCode")
    private String postalCode;

    @JsonProperty("avenue")
    private String avenue;

    @JsonProperty("complement")
    private String complement;

    @JsonProperty("number")
    private String number;

    @JsonProperty("city")
    private String city;

    @JsonProperty("district")
    private String district;

    @JsonProperty("state")
    private String state;

    // 1. No-Arg Constructor (Required by Jackson)
    public CommonUserResponseDTO() {
    }

    // 2. Methods matching your old Interface (Keeps your code working)
    public Long id() { return id; }
    public String name() { return name; }
    public String phone() { return phone; }
    public String email() { return email; }
    public Long patientId() { return patientId; }
    public Long doctorId() { return doctorId; }
    public String postalCode() { return postalCode; }
    public String avenue() { return avenue; }
    public String complement() { return complement; }
    public String number() { return number; }
    public String city() { return city; }
    public String district() { return district; }
    public String state() { return state; }

    // 3. Setters (Required for Deserialization)
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setAvenue(String avenue) { this.avenue = avenue; }
    public void setComplement(String complement) { this.complement = complement; }
    public void setNumber(String number) { this.number = number; }
    public void setCity(String city) { this.city = city; }
    public void setDistrict(String district) { this.district = district; }
    public void setState(String state) { this.state = state; }
}