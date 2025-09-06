package com.example.springauth.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
  private final int statusCode;
  private final String errorCode;

  public BusinessException(String message) {
    super(message);
    this.statusCode = 400;
    this.errorCode = "BUSINESS_ERROR";
  }

  public BusinessException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
    this.errorCode = "BUSINESS_ERROR";
  }

  public BusinessException(String message, int statusCode, String errorcode) {
    super(message);
    this.statusCode = statusCode;
    this.errorCode = errorcode;
  }
}
