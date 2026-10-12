package com.challenge.homedelivery.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.homedelivery.reservation.dto.CreateReservationRequest;
import com.challenge.homedelivery.reservation.dto.ReservationResponse;
import com.challenge.homedelivery.reservation.service.ReservationService;

import jakarta.validation.Valid;


@RestController 
public class ReservationController {
  private final ReservationService service;

  public ReservationController(ReservationService service) {
    this.service = service;
  }

  @PostMapping("/reservations")
  @ResponseStatus(HttpStatus.CREATED)
  public ReservationResponse create(@Valid @RequestBody CreateReservationRequest request) {
      return service.reserve(request);
  }
  
}
