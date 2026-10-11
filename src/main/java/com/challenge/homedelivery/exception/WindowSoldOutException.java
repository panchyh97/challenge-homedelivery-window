package com.challenge.homedelivery.exception;

public class WindowSoldOutException extends RuntimeException {
  public WindowSoldOutException(Long id) {
    super("Delivery window is sold out: " + id);
  }
}
