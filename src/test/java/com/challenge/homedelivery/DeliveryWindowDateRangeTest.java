package com.challenge.homedelivery;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.challenge.homedelivery.delivery.exception.DateOutOfRangeException;
import com.challenge.homedelivery.delivery.service.DeliveryQueryService;

@SpringBootTest
class DeliveryWindowDateRangeTest {
  private static final String COMMUNE = "La Serena";

  @Autowired
  DeliveryQueryService service;

  @Autowired
  Clock clock;

  @Test
  void rejectsPastDates() {
    LocalDate yesterday = LocalDate.now(clock).minusDays(1);
    assertThrows(DateOutOfRangeException.class, () -> service.getWindows(COMMUNE, yesterday));
  }

  @Test
  void rejectDatesBeyondTheBookingRange() {
    LocalDate tooFar = LocalDate.now(clock).plusDays(30);
    assertThrows(DateOutOfRangeException.class, () -> service.getWindows(COMMUNE, tooFar));
  }

  @Test
  void acceptsTodayAndTheLastBookableDay() {
    LocalDate today = LocalDate.now(clock);
    assertDoesNotThrow(() -> service.getWindows(COMMUNE, today));
    assertDoesNotThrow(() -> service.getWindows(COMMUNE, today.plusDays(6)));
  }
}
