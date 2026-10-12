package com.challenge.homedelivery.exception;

public class CommuneNotFoundException extends RuntimeException {
  public CommuneNotFoundException(String name) {
    super("Commune not found: " + name);
  }
}
