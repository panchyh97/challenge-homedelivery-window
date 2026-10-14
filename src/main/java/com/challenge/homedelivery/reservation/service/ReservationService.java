package com.challenge.homedelivery.reservation.service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.challenge.homedelivery.delivery.exception.CommuneNotFoundException;
import com.challenge.homedelivery.delivery.model.Commune;
import com.challenge.homedelivery.delivery.model.DeliveryWindow;
import com.challenge.homedelivery.delivery.repository.CommuneRepository;
import com.challenge.homedelivery.delivery.repository.DeliveryWindowRepository;
import com.challenge.homedelivery.reservation.dto.CreateReservationRequest;
import com.challenge.homedelivery.reservation.dto.ReservationResponse;
import com.challenge.homedelivery.reservation.exception.DuplicateOrderException;
import com.challenge.homedelivery.reservation.exception.WindowNotBookableException;
import com.challenge.homedelivery.reservation.exception.WindowNotFoundException;
import com.challenge.homedelivery.reservation.exception.WindowOutOfCoverageException;
import com.challenge.homedelivery.reservation.exception.WindowSoldOutException;
import com.challenge.homedelivery.reservation.model.Reservation;
import com.challenge.homedelivery.reservation.repository.ReservationRepository;

import jakarta.transaction.Transactional;

@Service
public class ReservationService {
  private final Duration minLeadTime;

  private final DeliveryWindowRepository windowRepository;
  private final CommuneRepository communeRepository;
  private final ReservationRepository reservationRepository;
  private final Clock clock;

  public ReservationService(DeliveryWindowRepository windowRepository, CommuneRepository communeRepository,
      ReservationRepository reservationRepository,
      Clock clock, @Value("${delivery.min-lead-time}") Duration minLeadTime) {
    this.windowRepository = windowRepository;
    this.communeRepository = communeRepository;
    this.reservationRepository = reservationRepository;
    this.clock = clock;
    this.minLeadTime = minLeadTime;
  }

  @Transactional
  public ReservationResponse reserve(CreateReservationRequest request) {
    LocalDateTime now = LocalDateTime.now(clock);

    DeliveryWindow window = windowRepository.findById(request.deliveryWindowId())
        .orElseThrow(() -> new WindowNotFoundException(request.deliveryWindowId()));

    Commune commune = communeRepository.findByNameIgnoreCase(request.commune())
        .orElseThrow(() -> new CommuneNotFoundException(request.commune()));

    if (!window.getZone().getId().equals(commune.getZone().getId())) {
      throw new WindowOutOfCoverageException(window.getId(), commune.getName());
    }
    ;

    if (!window.isBookable(now, minLeadTime)) {
      throw new WindowNotBookableException(window.getId());
    }

    Reservation reservation = new Reservation(window, request.orderReference(), request.address(), now);
    try {
      reservationRepository.saveAndFlush(reservation);
    } catch (DataIntegrityViolationException e) {
      throw new DuplicateOrderException(request.orderReference());
    }

    if (windowRepository.tryReserveSlot(window.getId()) == 0) {
      throw new WindowSoldOutException(window.getId());
    }

    return new ReservationResponse(
        reservation.getId(), window.getId(), reservation.getOrderReference(),
        reservation.getAddress(), reservation.getStatus(), reservation.getCreatedAt());
  }
}
