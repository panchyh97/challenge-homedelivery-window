package com.challenge.homedelivery.reservation.dto;

import java.time.LocalDateTime;

import com.challenge.homedelivery.reservation.model.ReservationStatus;

public record ReservationResponse(
    Long id,
    Long deliveryWindowId,
    String orderReference,
    String address,
    ReservationStatus status,
    LocalDateTime createdAt) {
}
