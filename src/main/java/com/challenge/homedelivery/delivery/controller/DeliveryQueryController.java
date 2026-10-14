package com.challenge.homedelivery.delivery.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.homedelivery.delivery.dto.CommuneResponse;
import com.challenge.homedelivery.delivery.dto.DateAvailabilityResponse;
import com.challenge.homedelivery.delivery.dto.DeliveryWindowResponse;
import com.challenge.homedelivery.delivery.dto.ZoneResponse;
import com.challenge.homedelivery.delivery.service.DeliveryQueryService;
import com.challenge.homedelivery.shared.web.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Delivery catalogue", description = "Read-only queries: zones, communes, dates and delivery windows available for a commune")
@RestController
public class DeliveryQueryController {
  private final DeliveryQueryService service;

  public DeliveryQueryController(DeliveryQueryService service) {
    this.service = service;
  }

  @Operation(summary = "List delivery zones", description = "Returns every geographic zone the service covers.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Zones retrieved")
  })
  @GetMapping("/zones")
  public ResponseEntity<ApiResponse<List<ZoneResponse>>> zones() {
    return ResponseEntity.ok(
        ApiResponse.success(HttpStatus.OK, "Zones retrieved", service.getZones()));
  }

  @Operation(summary = "List available dates for a commune", description = "Returns the next 7 days (today included) that have at least one bookable window "
      + "in the commune's zone. A window is bookable only if it starts after the minimum lead time "
      + "(1 hour by default), so today may be missing late in the day. "
      + "`soldOut` is true when every bookable window of that day is full.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Dates retrieved"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Missing `commune` parameter", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Commune not found", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
  })
  @GetMapping("/available-dates")
  public ResponseEntity<ApiResponse<List<DateAvailabilityResponse>>> availableDates(@RequestParam String commune) {
    return ResponseEntity.ok(
        ApiResponse.success(HttpStatus.OK, "Available dates retrieved", service.getAvailableDates(commune)));
  }

  @Operation(summary = "List delivery windows for a commune and date", description = "Returns the bookable windows of the commune's zone on the given date, ordered by start time, "
      + "with price and remaining slots. Windows that already passed the cut-off are not returned. "
      + "The date must be between today and 6 days ahead.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Windows retrieved (empty list if none are bookable)"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Missing parameter or date not in yyyy-MM-dd format", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Commune not found", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Date outside the bookable range (past or beyond 7 days)", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
  })
  @GetMapping("/delivery-windows")
  public ResponseEntity<ApiResponse<List<DeliveryWindowResponse>>> windows(
      @RequestParam String commune,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return ResponseEntity.ok(
        ApiResponse.success(HttpStatus.OK, "Delivery windows retrieved", service.getWindows(commune, date)));
  }

  @Operation(summary = "List communes", description = "Returns every commune with the zone it belongs to, ordered by name. "
      + "Use these names as the `commune` parameter of the other endpoints.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Communes retrieved")
  })
  @GetMapping("/communes")
  public ResponseEntity<ApiResponse<List<CommuneResponse>>> getCommunes() {
    return ResponseEntity.ok(
        ApiResponse.success(HttpStatus.OK, "Communes retrieved", service.getCommunes()));
  }

}
