package com.challenge.homedelivery.exception;

public class DuplicateOrderException extends RuntimeException {
  public DuplicateOrderException(String orderReference) {
    super("Order already has a reservation: " + orderReference);
  }
}
