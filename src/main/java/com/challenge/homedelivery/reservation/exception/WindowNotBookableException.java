package com.challenge.homedelivery.reservation.exception;

import org.springframework.http.HttpStatus;

import com.challenge.homedelivery.shared.exception.ApiException;

public class WindowNotBookableException extends ApiException{
  public WindowNotBookableException(Long id) {
    super("Window " + id + " is no longer available", HttpStatus.UNPROCESSABLE_CONTENT);
  }
}
