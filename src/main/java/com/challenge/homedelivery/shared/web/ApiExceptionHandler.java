package com.challenge.homedelivery.shared.web;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.challenge.homedelivery.delivery.exception.CommuneNotFoundException;
import com.challenge.homedelivery.reservation.exception.DuplicateOrderException;
import com.challenge.homedelivery.reservation.exception.WindowAlreadyStartedException;
import com.challenge.homedelivery.reservation.exception.WindowNotFoundException;
import com.challenge.homedelivery.reservation.exception.WindowSoldOutException;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler({ CommuneNotFoundException.class, WindowNotFoundException.class })
  public ResponseEntity<Map<String, String>> notFound(RuntimeException ex) {
    return error(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler({ WindowSoldOutException.class, DuplicateOrderException.class })
  public ResponseEntity<Map<String, String>> conflict(RuntimeException ex) {
    return error(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(WindowAlreadyStartedException.class)
  public ResponseEntity<Map<String, String>> unprocessable(WindowAlreadyStartedException ex) {
    return error(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> invalidRequest(MethodArgumentNotValidException ex) {
    Map<String, String> fields = ex.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            fe -> String.valueOf(fe.getDefaultMessage()),
            (first, second) -> first));
    return ResponseEntity.badRequest().body(Map.of("error", "Invalid request", "fields", fields));
  }

  private ResponseEntity<Map<String, String>> error(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(Map.of("error", message));
  }
}
