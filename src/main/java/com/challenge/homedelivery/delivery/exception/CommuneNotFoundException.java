package com.challenge.homedelivery.delivery.exception;

import org.springframework.http.HttpStatus;

import com.challenge.homedelivery.shared.exception.ApiException;

public class CommuneNotFoundException extends ApiException {
  public CommuneNotFoundException(String name) {
    super("Commune " + name + " not found", HttpStatus.NOT_FOUND);
  }
}
