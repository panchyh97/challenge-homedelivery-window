package com.challenge.homedelivery.reservation.exception;

public class WindowSoldOutException extends RuntimeException {
  public WindowSoldOutException(Long id) {
    super("Delivery window is sold out: " + id);
  }
}
