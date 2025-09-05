package com.example.springauth.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 로그인 요청을 처리하는 DTO record 특징 - Java 16부터 도입된 불변 데이터 전용 클래스 - 모든 필드는 private final 로 자동 생성 - 생성자,
 * getter(대신 필드명 메서드), equals/hashCode/toString 자동 생성 - DTO/VO 같은 데이터 전달용 객체에 적합
 */
public record LoginRequest(
    // @NotBlank : null이거나 빈 문자열이면 검증 에러 발생
    @NotBlank(message = "Username is required") String username,
    @NotBlank(message = "Password is required") String password) {
  public LoginRequest {
    /*
     * Compact Constructor (레코드 압축 생성자)
     * - 모든 필드 초기화 시 실행됨
     * - @NotBlank가 null/빈문자열을 체크하지만,
     *   여기서는 공백만 있는 경우도 방어적으로 체크
     */
    if (username != null && username.trim().isEmpty()) { // username이 null은 아니지만 공백만 들어온 경우
      throw new IllegalArgumentException("Username cannot be blank");
    }
    if (password != null && password.trim().isEmpty()) { // password가 null은 아니지만 공백만 들어온 경우
      throw new IllegalArgumentException("Password cannot be blank");
    }
  }
}
