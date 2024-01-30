package com.rlaclgh.onetomany.config;


import com.rlaclgh.onetomany.constant.ErrorCode;
import com.rlaclgh.onetomany.exception.CustomException;
import com.rlaclgh.onetomany.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(ErrorResponse.builder()
        .status(ex.getErrorCode().getHttpStatus().value())
        .code(ex.getErrorCode().name())
        .message(ex.getDescription())
        .build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(MethodArgumentNotValidException ex) {

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
        .status(ErrorCode.BAD_REQUEST.getHttpStatus().value())
        .code(ErrorCode.BAD_REQUEST.name())
        .message(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage())
        .build());
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ErrorResponse> handleGeneralExceptions(Exception ex) {

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        ErrorResponse.builder()
            .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value())
            .code(ErrorCode.INTERNAL_SERVER_ERROR.name())
            .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
            .build()
    );
  }


  @ExceptionHandler(AsyncRequestTimeoutException.class)
  @ResponseBody
  public SseEmitter sseTimeoutException(AsyncRequestTimeoutException e) {
    log.error("SSE TIMEOUT EXCEPTION:{}", e.getMessage(), e);
    return null;
  }


}
