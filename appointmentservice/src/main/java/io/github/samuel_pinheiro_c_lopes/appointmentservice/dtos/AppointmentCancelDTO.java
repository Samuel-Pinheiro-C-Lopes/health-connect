package io.github.samuel_pinheiro_c_lopes.appointmentservice.dtos;

import io.github.samuel_pinheiro_c_lopes.appointmentservice.models.enums.CancelReason;

public record AppointmentCancelDTO(
        CancelReason reason
) {}
