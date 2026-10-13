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

@RestController
public class DeliveryQueryController {
  private final DeliveryQueryService service;

  public DeliveryQueryController(DeliveryQueryService service) {
    this.service = service;
  }

  @GetMapping("/zones")
  public ResponseEntity<ApiResponse<List<ZoneResponse>>> zones() {
    return ResponseEntity.ok(
        ApiResponse.success(HttpStatus.OK, "Zones retrieved", service.getZones()));
  }

  @GetMapping("/available-dates")
  public ResponseEntity<ApiResponse<List<DateAvailabilityResponse>>> availableDates(@RequestParam String commune) {
    return ResponseEntity.ok(
        ApiResponse.success(HttpStatus.OK, "Available dates retrieved", service.getAvailableDates(commune)));
  }

  @GetMapping("/delivery-windows")
  public ResponseEntity<ApiResponse<List<DeliveryWindowResponse>>> windows(
      @RequestParam String commune,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return ResponseEntity.ok(
        ApiResponse.success(HttpStatus.OK, "Delivery windows retrieved", service.getWindows(commune, date)));
  }

  @GetMapping("/communes")
  public ResponseEntity<ApiResponse<List<CommuneResponse>>> getCommunes() {
    return ResponseEntity.ok(
        ApiResponse.success(HttpStatus.OK, "Communes retrieved", service.getCommunes()));
  }

}
