package com.example.springauth.auth.dto;

/** 로그인 응답을 처리하는 DTO accessToken, refreshToken, tokenType을 반환함 */
public record LoginResponse(String accessToken, String refreshToken, String tokenType) {

  // 정적 팩토리 메서드 : new 대신 객체 생성을 담당하는 static 메서드
  public static LoginResponse of(String accessToken, String refreshToken) {
    // 로그인 성공 시, tokenType은 항상 Bearer로 고정
    return new LoginResponse(accessToken, refreshToken, "Bearer");
  }
}
