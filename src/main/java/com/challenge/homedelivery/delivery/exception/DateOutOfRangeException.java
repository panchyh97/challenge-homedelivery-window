package com.challenge.homedelivery.delivery.exception;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;

import com.challenge.homedelivery.shared.exception.ApiException;

public class DateOutOfRangeException extends ApiException {
  public DateOutOfRangeException(LocalDate requested, LocalDate first, LocalDate last) {
    super("Date " + requested + " is outside the booking range (" + first + " to " + last,
        HttpStatus.UNPROCESSABLE_CONTENT);
  }

}
