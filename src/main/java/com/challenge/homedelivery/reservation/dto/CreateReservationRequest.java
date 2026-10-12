package com.challenge.homedelivery.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReservationRequest(
    @NotNull Long deliveryWindowId,
    @NotBlank @Size(max = 50) String orderReference,
    @NotBlank @Size(max = 255) String address) {
}
