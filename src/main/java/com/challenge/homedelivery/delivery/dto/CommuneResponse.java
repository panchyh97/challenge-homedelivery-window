package com.challenge.homedelivery.delivery.dto;

import com.challenge.homedelivery.delivery.model.Commune;

public record CommuneResponse(Long id, String name, String zoneName) {
  public static CommuneResponse from(Commune commune) {
    return new CommuneResponse(commune.getId(), commune.getName(), commune.getZone().getName());
  }
}
