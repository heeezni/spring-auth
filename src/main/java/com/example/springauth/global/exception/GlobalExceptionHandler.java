package com.example.springauth.global.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice // 모든 Controller에서 발생한 예외를 잡아서 처리
public class GlobalExceptionHandler {

  /** 잘못된 인자 전달 시 (예: 값이 null인데 들어옴) */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    log.error("IllegalArgumentException : ", e);
    ErrorResponse errorResponse = ErrorResponse.of(e.getMessage(), "INVALID_ARGUMENT");
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // 400 반환
  }

  /** 도메인/비즈니스 로직에서 발생하는 커스텀 예외 처리 */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
    log.error("BusinessException : ", e);
    ErrorResponse errorResponse = ErrorResponse.of(e.getMessage(), e.getErrorCode());
    return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(e.getStatusCode()));
  }

  // ✅ 로그인 실패, 잘못된 인증 정보
  @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
  public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception e) {
    log.error("AuthenticationException: ", e);
    ErrorResponse errorResponse =
        ErrorResponse.of("Authentication failed", "AUTHENTICATION_FAILED");
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED); // 401
  }

  // 인증이 안 되었는데 리소스를 요청하거나, 잘못된 상태일 때
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
    log.error("IllegalStateException: ", e);
    if (e.getMessage() != null && e.getMessage().contains("not authenticated")) {
      // 인증이 필요한데 인증이 안 된 경우
      ErrorResponse errorResponse =
          ErrorResponse.of("Authentication required", "AUTHENTICATION_REQUIRED");
      return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED); // 401
    }
    // 그 외의 상태 문제
    ErrorResponse errorResponse = ErrorResponse.of(e.getMessage(), "ILLEGAL_STATE");
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // 400
  }

  // 권한 부족 (로그인은 됐지만 권한이 없을 때)
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
    log.error("AccessDeniedException: ", e);
    ErrorResponse errorResponse = ErrorResponse.of("Access denied", "ACCESS_DENIED");
    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN); // 403
  }

  // DTO 검증(@Valid) 실패 → 필드별 에러 메시지 반환
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException: ", e);
    // 필드별 에러 메시지 추출
    List<String> errors =
        e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.toList());
    ErrorResponse errorResponse = ErrorResponse.of("Validation failed", "VALIDATION_ERROR", errors);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // 400
  }

  // 바인딩 실패 (타입 변환 오류, 잘못된 요청 파라미터 등)
  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
    log.error("BindException: ", e);
    List<String> errors =
        e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.toList());

    ErrorResponse errorResponse = ErrorResponse.of("Binding failed", "BINDING_ERROR", errors);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // 400
  }

  // 마지막 안전망: 처리되지 않은 모든 예외를 잡음
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
    log.error("Unexcepted error: ", e);

    ErrorResponse errorResponse =
        ErrorResponse.of("An unexpected error occurred", "INTERNAL_ERROR");
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR); // 500
  }
}
