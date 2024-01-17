package com.rlaclgh.onetomany.config;


import com.rlaclgh.onetomany.constant.ErrorCode;
import com.rlaclgh.onetomany.exception.CustomException;
import com.rlaclgh.onetomany.exception.EmailDuplicatedException;
import com.rlaclgh.onetomany.response.ErrorResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.coyote.BadRequestException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(ErrorResponse.builder()
        .status(ex.getErrorCode().getHttpStatus().value())
        .code(ex.getErrorCode().name())
        .message(ex.getDescription())
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, List<String>>> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult().getFieldErrors()
        .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handleBadCredentialsErrors(
      BadCredentialsException ex) {

    return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(EmailDuplicatedException.class)
  public ResponseEntity<String> handleEmailDuplicatedException(
      EmailDuplicatedException ex) {
    return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> handleBadRequestException(
      BadRequestException ex) {

    return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFoundException(
      NotFoundException ex) {

    return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleNotFoundException(
      UsernameNotFoundException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.NOT_FOUND);
  }


  @ExceptionHandler(RuntimeException.class)
  public final ResponseEntity<Map<String, List<String>>> handleRuntimeExceptions(
      RuntimeException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private Map<String, List<String>> getErrorsMap(List<String> errors) {
    Map<String, List<String>> errorResponse = new HashMap<>();
    errorResponse.put("errors", errors);
    return errorResponse;
  }


}
