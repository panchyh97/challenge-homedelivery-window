package com.challenge.homedelivery.delivery.dto;

import java.time.LocalTime;

public record DeliveryWindowResponse(
  Long id,
  LocalTime startTime,
  LocalTime endTime,
  int price,
  int availableSlots,
  boolean soldOut
){}
