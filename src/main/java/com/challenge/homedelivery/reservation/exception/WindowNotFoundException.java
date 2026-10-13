package com.challenge.homedelivery.reservation.exception;

import org.springframework.http.HttpStatus;

import com.challenge.homedelivery.shared.exception.ApiException;

public class WindowNotFoundException extends ApiException {
  public WindowNotFoundException(Long id) {
    super("Delivery window " + id + " not found", HttpStatus.NOT_FOUND);
  }
  
}
