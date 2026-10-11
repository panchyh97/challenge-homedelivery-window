package com.challenge.homedelivery.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.challenge.homedelivery.dto.DateAvailabilityResponse;
import com.challenge.homedelivery.dto.DeliveryWindowResponse;
import com.challenge.homedelivery.dto.ZoneResponse;
import com.challenge.homedelivery.model.DeliveryWindow;
import com.challenge.homedelivery.repository.CommuneRepository;
import com.challenge.homedelivery.repository.DeliveryWindowRepository;
import com.challenge.homedelivery.repository.ZoneRepository;

@Service 
@Transactional (readOnly = true)
public class DeliveryQueryService {
  private static final int DAYS_AHEAD = 7;

  private final ZoneRepository zoneRepository;
  private final CommuneRepository communeRepository;
  private final DeliveryWindowRepository windowRepository;

  public DeliveryQueryService(ZoneRepository zoneRepository, CommuneRepository communeRepository,
      DeliveryWindowRepository windowRepository) {
    this.zoneRepository = zoneRepository;
    this.communeRepository = communeRepository;
    this.windowRepository = windowRepository;
  }

  public List<ZoneResponse> getZones() {
    return zoneRepository.findAll().stream()
        .map(z -> new ZoneResponse(z.getId(), z.getName()))
        .toList();
  }

  public List<DateAvailabilityResponse> getAvailableDates(String communeName) {
    Long zoneId = zoneIdOf(communeName);
    LocalDate today = LocalDate.now();
    LocalTime now = LocalTime.now();

    List<DeliveryWindow> windows = windowRepository
        .findByZoneIdAndDeliveryDateBetweenOrderByDeliveryDateAscStartTimeAsc(zoneId, today,
            today.plusDays(DAYS_AHEAD - 1));

    Map<LocalDate, Integer> slotsPerDate = windows.stream()
    .filter(w -> isStillOffered(w, today, now))
    .collect(Collectors.groupingBy(
      DeliveryWindow::getDeliveryDate,
          TreeMap::new,
              Collectors.summingInt(DeliveryWindow::getAvailableSlots)
        ));

    return slotsPerDate.entrySet().stream()
    .map(e -> new DateAvailabilityResponse(e.getKey(), e.getValue() == 0, e.getValue()))
    .toList();
  }

  public List<DeliveryWindowResponse> getWindows(String communeName, LocalDate date) {
        Long zoneId = zoneIdOf(communeName);
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        return windowRepository.findByZoneIdAndDeliveryDateOrderByStartTimeAsc(zoneId, date).stream()
          .filter(w -> isStillOffered(w, today, now))
          .map(w -> new DeliveryWindowResponse(
                  w.getId(), w.getStartTime(), w.getEndTime(),
                  w.getPrice(), w.getAvailableSlots(), w.isSoldOut()))
          .toList();
    }

    private Long zoneIdOf(String communeName) {
        return communeRepository.findByNameIgnoreCase(communeName)
          .map(c -> c.getZone().getId())
          .orElseThrow(() -> new ResponseStatusException(
                  HttpStatus.NOT_FOUND, "Commune not found: " + communeName));
    }

    private boolean isStillOffered(DeliveryWindow w, LocalDate today, LocalTime now) {
        LocalDate date = w.getDeliveryDate();
        return date.isAfter(today) || (date.equals(today) && w.getStartTime().isAfter(now));
    }
}