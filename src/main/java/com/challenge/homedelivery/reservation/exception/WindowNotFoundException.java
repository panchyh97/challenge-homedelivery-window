package com.challenge.homedelivery.reservation.exception;

public class WindowNotFoundException extends RuntimeException {
  public WindowNotFoundException(Long id) {
    super("Delivery window not found: " + id);
  }
  
}
