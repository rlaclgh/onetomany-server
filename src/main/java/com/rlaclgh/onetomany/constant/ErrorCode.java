package com.rlaclgh.onetomany.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

  // 400 BAD_REQUEST : 잘못된 요청
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

  // 401 UNAUTHORIZED : 권한 없음
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

  // 403 FORBIDDEN : 접근 금지
  FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 금지되었습니다."),

  // 404 NOT_FOUND : 리소스를 찾을 수 없음
  NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

  // 500 INTERNAL_SERVER_ERROR : 내부 서버 오류
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다.");


  private final HttpStatus httpStatus;
  private final String message;


}
