package com.challenge.homedelivery.delivery.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.homedelivery.delivery.dto.DateAvailabilityResponse;
import com.challenge.homedelivery.delivery.dto.DeliveryWindowResponse;
import com.challenge.homedelivery.delivery.dto.ZoneResponse;
import com.challenge.homedelivery.delivery.service.DeliveryQueryService;


@RestController
public class DeliveryQueryController {
  private final DeliveryQueryService service;

  public DeliveryQueryController(DeliveryQueryService service) {
    this.service = service;
  }

  @GetMapping("/zones")
  public List<ZoneResponse> zones() {
    return service.getZones();
  }

  @GetMapping("/available-dates")
  public List<DateAvailabilityResponse> availableDates(@RequestParam String commune) {
      return service.getAvailableDates(commune);
  }

  @GetMapping("/delivery-windows")
  public List<DeliveryWindowResponse> windows(
          @RequestParam String commune,
          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
      return service.getWindows(commune, date);
  }
}
