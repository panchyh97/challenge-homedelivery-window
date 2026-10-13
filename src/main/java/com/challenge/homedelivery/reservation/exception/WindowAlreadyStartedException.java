package com.challenge.homedelivery.reservation.exception;

import org.springframework.http.HttpStatus;

import com.challenge.homedelivery.shared.exception.ApiException;

public class WindowAlreadyStartedException extends ApiException{
  public WindowAlreadyStartedException(Long id) {
    super("Delivery window " + id + " has already started", HttpStatus.UNPROCESSABLE_CONTENT);
  }
}
