package com.challenge.homedelivery.reservation.exception;

import org.springframework.http.HttpStatus;

import com.challenge.homedelivery.shared.exception.ApiException;

public class WindowSoldOutException extends ApiException {
  public WindowSoldOutException(Long id) {
    super("Delivery window" + id + " is sold out", HttpStatus.CONFLICT);
  }
}
