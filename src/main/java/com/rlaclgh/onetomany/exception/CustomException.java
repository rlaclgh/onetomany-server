package com.rlaclgh.onetomany.exception;

import com.rlaclgh.onetomany.constant.ErrorCode;
import lombok.Getter;


@Getter
public class CustomException extends RuntimeException {

  ErrorCode errorCode;
  String description;

  public CustomException(ErrorCode errorCode, String description) {
    this.errorCode = errorCode;
    this.description = description;
  }

  public CustomException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.description = errorCode.getMessage();
  }
}
