package com.example.springauth.global.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
  private String result; // 결과 상태 (예: "ERROR")
  private String message; // 에러 메세지
  private String errorCode; // 에러 코드 (INVALID_ARGUMENT, AUTHENTICATION_FAILED 등)
  private List<String> details; // 상세 에러 목록
  private LocalDateTime timestamp; // 발생 시각

  // 상세 오류 없이 응답 생성
  public static ErrorResponse of(String message, String errorCode) {
    return new ErrorResponse("ERROR", message, errorCode, null, LocalDateTime.now());
  }

  // 상세 오류 포함 응답 생성
  public static ErrorResponse of(String message, String errorCode, List<String> details) {
    return new ErrorResponse("ERROR", message, errorCode, details, LocalDateTime.now());
  }
}
