package com.challenge.homedelivery.service;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.challenge.homedelivery.dto.CreateReservationRequest;
import com.challenge.homedelivery.dto.ReservationResponse;
import com.challenge.homedelivery.exception.DuplicateOrderException;
import com.challenge.homedelivery.exception.WindowAlreadyStartedException;
import com.challenge.homedelivery.exception.WindowNotFoundException;
import com.challenge.homedelivery.exception.WindowSoldOutException;
import com.challenge.homedelivery.model.DeliveryWindow;
import com.challenge.homedelivery.model.Reservation;
import com.challenge.homedelivery.repository.DeliveryWindowRepository;
import com.challenge.homedelivery.repository.ReservationRepository;

import jakarta.transaction.Transactional;

@Service
public class ReservationService {
  private final DeliveryWindowRepository windowRepository;
  private final ReservationRepository reservationRepository;
  private final Clock clock;

  public ReservationService(DeliveryWindowRepository windowRepository, ReservationRepository reservationRepository,
      Clock clock) {
    this.windowRepository = windowRepository;
    this.reservationRepository = reservationRepository;
    this.clock = clock;
  }

  @Transactional
  public ReservationResponse reserve(CreateReservationRequest request) {
    LocalDateTime now = LocalDateTime.now(clock);

    DeliveryWindow window = windowRepository.findById(request.deliveryWindowId())
        .orElseThrow(() -> new WindowNotFoundException(request.deliveryWindowId()));

    if (window.hasStarted(now)) {
      throw new WindowAlreadyStartedException(window.getId());
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
          reservation.getAddress(), reservation.getStatus(), reservation.getCreatedAt()
    );
  }
}
