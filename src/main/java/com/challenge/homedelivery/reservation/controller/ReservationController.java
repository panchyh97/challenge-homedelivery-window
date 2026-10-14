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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Reservations", description = "Book a delivery window")
@RestController
public class ReservationController {
  private final ReservationService service;

  public ReservationController(ReservationService service) {
    this.service = service;
  }

  @Operation(summary = "Create a reservation", description = "Reserves one slot of a delivery window. The window must belong to the commune's zone, start after minimum lead time.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Reservation confirmed"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid or malformed request", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Window or commune not found", content = @Content),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Window sold out or duplicate order reference", content = @Content),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Window out of coverage, not bookable anymore or date out of range", content = @Content)
  })
  @PostMapping("/reservations")
  public ResponseEntity<ApiResponse<ReservationResponse>> create(
      @Valid @RequestBody CreateReservationRequest request) {
    ReservationResponse result = service.reserve(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(HttpStatus.CREATED, "Reservation created", result));
  }

}
