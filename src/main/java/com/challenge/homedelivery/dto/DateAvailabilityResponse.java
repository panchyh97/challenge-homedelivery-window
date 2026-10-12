package com.challenge.homedelivery.dto;

import java.time.LocalDate;

public record DateAvailabilityResponse(LocalDate date, boolean soldOut, int availableSlots) {}