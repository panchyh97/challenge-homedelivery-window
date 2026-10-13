package com.challenge.homedelivery.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.homedelivery.reservation.dto.CreateReservationRequest;
import com.challenge.homedelivery.reservation.dto.ReservationResponse;
import com.challenge.homedelivery.reservation.service.ReservationService;
import com.challenge.homedelivery.shared.web.ApiResponse;

import jakarta.validation.Valid;

@RestController
public class ReservationController {
  private final ReservationService service;

  public ReservationController(ReservationService service) {
    this.service = service;
  }

  @PostMapping("/reservations")
  public ResponseEntity<ApiResponse<ReservationResponse>> create(
      @Valid @RequestBody CreateReservationRequest request) {
    ReservationResponse result = service.reserve(request);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.success(HttpStatus.CREATED, "Reservation created", result));
  }

}
