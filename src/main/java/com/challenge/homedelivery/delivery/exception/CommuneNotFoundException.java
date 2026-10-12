package com.challenge.homedelivery.delivery.exception;

public class CommuneNotFoundException extends RuntimeException {
  public CommuneNotFoundException(String name) {
    super("Commune not found: " + name);
  }
}
