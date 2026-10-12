package com.challenge.homedelivery.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.challenge.homedelivery.model.DeliveryWindow;

public interface DeliveryWindowRepository extends JpaRepository<DeliveryWindow, Long> {
  List<DeliveryWindow> findByZoneIdAndDeliveryDateBetweenOrderByDeliveryDateAscStartTimeAsc(
      Long zoneId, LocalDate from, LocalDate to);

  List<DeliveryWindow> findByZoneIdAndDeliveryDateOrderByStartTimeAsc(
      Long zoneId, LocalDate date);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE DeliveryWindow w SET w.reservedCount = w.reservedCount + 1 " +
      "WHERE w.id = :id AND w.reservedCount < w.capacity")
  int tryReserveSlot(@Param("id") Long id);
}
