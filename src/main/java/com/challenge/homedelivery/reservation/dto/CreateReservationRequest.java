package com.challenge.homedelivery.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReservationRequest(
    @NotNull(message = "must not be null") Long deliveryWindowId,

    @NotBlank(message = "must not be blank") @Size(max = 50, message = "must be at most 50 characters") String orderReference,

    @NotBlank(message = "must not be blank") @Size(max = 255, message = "must be at most 255 characters") String address,

    @NotBlank(message = "must not be blank") @Size(max = 100, message = "must be at most 100 characters") String commune) {
}
