package com.challenge.homedelivery.reservation.exception;

import org.springframework.http.HttpStatus;

import com.challenge.homedelivery.shared.exception.ApiException;

public class WindowOutOfCoverageException extends ApiException {
  public WindowOutOfCoverageException(Long windowId, String communeName) {
    super("Delivery window " + windowId + " does not cover commune " + communeName, HttpStatus.UNPROCESSABLE_CONTENT);
  }
}
