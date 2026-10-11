package com.challenge.homedelivery.dto;

import java.time.LocalDateTime;

import com.challenge.homedelivery.model.ReservationStatus;

public record ReservationResponse(
    Long id,
    Long deliveryWindowId,
    String orderReference,
    String address,
    ReservationStatus status,
    LocalDateTime createdAt) {
}
