package com.challenge.homedelivery.exception;

public class WindowAlreadyStartedException extends RuntimeException{
  public WindowAlreadyStartedException(Long id) {
    super("Delivery window has already started: " +id );
  }
}
