package com.challenge.homedelivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.challenge.homedelivery.delivery.exception.CommuneNotFoundException;
import com.challenge.homedelivery.delivery.model.DeliveryWindow;
import com.challenge.homedelivery.delivery.repository.CommuneRepository;
import com.challenge.homedelivery.delivery.repository.DeliveryWindowRepository;
import com.challenge.homedelivery.reservation.dto.CreateReservationRequest;
import com.challenge.homedelivery.reservation.dto.ReservationResponse;
import com.challenge.homedelivery.reservation.exception.WindowOutOfCoverageException;
import com.challenge.homedelivery.reservation.service.ReservationService;

@SpringBootTest
@Transactional
class ReservationCoverageTest {
  @Autowired
  ReservationService reservationService;

  @Autowired
  DeliveryWindowRepository windowRepository;

  @Autowired
  CommuneRepository communeRepository;

  @Autowired
  Clock clock;

  @Test
  void rejectsReservationWhenWindowBelongsToAnotherZone() {
    DeliveryWindow northWindow = firstFutureWindowOf("La Serena");

    var request = new CreateReservationRequest(
        northWindow.getId(), "ORDER-COVERAGE-1", "Some street 123, Santiago", "Santiago");

    assertThrows(WindowOutOfCoverageException.class, () -> reservationService.reserve(request));
  }

  @Test
  void acceptsReservationWhenCommuneIsInTheSameZoneAsTheWindow() {
    DeliveryWindow northWindow = firstFutureWindowOf("La Serena");

    var request = new CreateReservationRequest(northWindow.getId(), "ORDER-COVERAGE-2", "Some street 123, Coquimbo",
        "Coquimbo");

    ReservationResponse response = reservationService.reserve(request);

    assertEquals(northWindow.getId(), response.deliveryWindowId());
  }

  @Test
  void rejectReservationWhenCommuneDoesNotExist() {
    DeliveryWindow northWindow = firstFutureWindowOf("La Serena");

    var request = new CreateReservationRequest(northWindow.getId(), "ORDER-COVERAGE-3", "Somewhere", "El Salvador");

    assertThrows(CommuneNotFoundException.class, () -> reservationService.reserve(request));
  }

  private DeliveryWindow firstFutureWindowOf(String communeName) {
    Long zoneId = communeRepository.findByNameIgnoreCase(communeName)
        .orElseThrow().getZone().getId();

    LocalDate tomorrow = LocalDate.now(clock).plusDays(1);
    return windowRepository
        .findByZoneIdAndDeliveryDateBetweenOrderByDeliveryDateAscStartTimeAsc(zoneId, tomorrow, tomorrow.plusDays(5))
        .stream()
        .filter(w -> !w.isSoldOut())
        .findFirst()
        .orElseThrow();
  }
}
