package com.challenge.homedelivery.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge.homedelivery.reservation.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{}
