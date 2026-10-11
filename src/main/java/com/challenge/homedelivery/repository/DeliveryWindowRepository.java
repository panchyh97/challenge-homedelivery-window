package com.challenge.homedelivery.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge.homedelivery.model.DeliveryWindow;

public interface DeliveryWindowRepository extends JpaRepository<DeliveryWindow, Long> {
  List<DeliveryWindow> findByZoneIdAndDeliveryDateBetweenOrderByDeliveryDateAscStartTimeAsc(
    Long zoneId, LocalDate from, LocalDate to);

  List<DeliveryWindow> findByZoneIdAndDeliveryDateOrderByStartTimeAsc(
    Long zoneId, LocalDate date);
  
}
