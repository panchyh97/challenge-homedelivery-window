package com.challenge.homedelivery.dto;

import com.challenge.homedelivery.model.Commune;

public record CommuneResponse(Long id, String name, String zoneName) {
  public static CommuneResponse from(Commune commune) {
    return new CommuneResponse(commune.getId(), commune.getName(), commune.getZone().getName());
  }
}
