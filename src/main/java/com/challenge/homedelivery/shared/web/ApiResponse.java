package com.challenge.homedelivery.shared.web;

import java.util.Map;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(int status, String message, T data, String error, Map<String, String> fields) {
  public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
    return new ApiResponse<T>(status.value(), message, data, null, null);
  };

  public static ApiResponse<Void> error(HttpStatus status, String message, Map<String, String> fields) {
    return new ApiResponse<Void>(status.value(), message, null, status.getReasonPhrase(), fields);
  }
}
