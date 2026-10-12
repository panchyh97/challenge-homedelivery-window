package com.challenge.homedelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge.homedelivery.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{}
