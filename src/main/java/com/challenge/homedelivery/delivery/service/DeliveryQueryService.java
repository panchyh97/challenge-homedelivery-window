package com.challenge.homedelivery.delivery.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.challenge.homedelivery.delivery.dto.DateAvailabilityResponse;
import com.challenge.homedelivery.delivery.dto.DeliveryWindowResponse;
import com.challenge.homedelivery.delivery.dto.ZoneResponse;
import com.challenge.homedelivery.delivery.exception.CommuneNotFoundException;
import com.challenge.homedelivery.delivery.model.DeliveryWindow;
import com.challenge.homedelivery.delivery.repository.CommuneRepository;
import com.challenge.homedelivery.delivery.repository.DeliveryWindowRepository;
import com.challenge.homedelivery.delivery.repository.ZoneRepository;

@Service
@Transactional(readOnly = true)
public class DeliveryQueryService {
  private static final int DAYS_AHEAD = 7;

  private final ZoneRepository zoneRepository;
  private final CommuneRepository communeRepository;
  private final DeliveryWindowRepository windowRepository;
  private final Clock clock;

  public DeliveryQueryService(ZoneRepository zoneRepository, CommuneRepository communeRepository,
      DeliveryWindowRepository windowRepository, Clock clock) {
    this.zoneRepository = zoneRepository;
    this.communeRepository = communeRepository;
    this.windowRepository = windowRepository;
    this.clock = clock;
  }

  public List<ZoneResponse> getZones() {
    return zoneRepository.findAll().stream()
        .map(z -> new ZoneResponse(z.getId(), z.getName()))
        .toList();
  }

  public List<DateAvailabilityResponse> getAvailableDates(String communeName) {
    Long zoneId = zoneIdOf(communeName);
    LocalDateTime now = LocalDateTime.now(clock);
    LocalDate today = now.toLocalDate();

    List<DeliveryWindow> windows = windowRepository
        .findByZoneIdAndDeliveryDateBetweenOrderByDeliveryDateAscStartTimeAsc(zoneId, today,
            today.plusDays(DAYS_AHEAD - 1));

    Map<LocalDate, Integer> slotsPerDate = windows.stream()
        .filter(w -> !w.hasStarted(now))
        .collect(Collectors.groupingBy(
            DeliveryWindow::getDeliveryDate,
            TreeMap::new,
            Collectors.summingInt(DeliveryWindow::getAvailableSlots)));

    return slotsPerDate.entrySet().stream()
        .map(e -> new DateAvailabilityResponse(e.getKey(), e.getValue() == 0, e.getValue()))
        .toList();
  }

  public List<DeliveryWindowResponse> getWindows(String communeName, LocalDate date) {
    Long zoneId = zoneIdOf(communeName);
    LocalDateTime now = LocalDateTime.now(clock);

    return windowRepository.findByZoneIdAndDeliveryDateOrderByStartTimeAsc(zoneId, date).stream()
        .filter(w -> !w.hasStarted(now))
        .map(w -> new DeliveryWindowResponse(
            w.getId(), w.getStartTime(), w.getEndTime(),
            w.getPrice(), w.getAvailableSlots(), w.isSoldOut()))
        .toList();
  }

  private Long zoneIdOf(String communeName) {
    return communeRepository.findByNameIgnoreCase(communeName)
        .map(c -> c.getZone().getId())
        .orElseThrow(() -> new CommuneNotFoundException(communeName));
  }

}