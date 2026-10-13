package com.challenge.homedelivery.reservation.exception;

import org.springframework.http.HttpStatus;

import com.challenge.homedelivery.shared.exception.ApiException;

public class DuplicateOrderException extends ApiException {
  public DuplicateOrderException(String orderReference) {
    super("Order " + orderReference + " already has a reservation", HttpStatus.CONFLICT);
  }
}
