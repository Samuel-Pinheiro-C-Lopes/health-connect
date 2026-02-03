package io.github.samuel_pinheiro_c_lopes.userservice.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.samuel_pinheiro_c_lopes.userservice.models.Address;
import io.github.samuel_pinheiro_c_lopes.userservice.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRequestDTO(
        @NotBlank
        @Email
        @JsonProperty("email")
        String email,
        @NotBlank
        @JsonProperty("password")
        String password,
        Long doctorId,
        Long patientId,
        @NotBlank
        @JsonProperty("name")
        String name,
        @NotNull
        @NotBlank
        @Pattern(regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}$", message = "Invalid phone format")
        @JsonProperty("phone")
        String phone,
        @NotBlank
        @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "Invalid CEP format")
        @JsonProperty("postalCode")
        String postalCode,
        @NotBlank
        @JsonProperty("avenue")
        String avenue,
        @NotBlank
        @JsonProperty("complement")
        String complement,
        @NotBlank
        @JsonProperty("number")
        String number,
        @NotBlank
        @JsonProperty("city")
        String city,
        @NotBlank
        @JsonProperty("district")
        String district, 
        @NotBlank
        @JsonProperty("state")
        String state
) {
    public User toUser() {
        return new User(
            email(), 
            password(),
            doctorId(),
            patientId(),
            name(), 
            phone(), 
            new Address(
                postalCode(), 
                avenue(), 
                complement(), 
                number(), 
                city(), 
                district(), 
                state()
            )
        );
    }
}