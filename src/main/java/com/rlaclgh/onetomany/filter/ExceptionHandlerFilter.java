package com.rlaclgh.onetomany.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rlaclgh.onetomany.constant.ErrorCode;
import com.rlaclgh.onetomany.exception.CustomException;
import com.rlaclgh.onetomany.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    } catch (CustomException ex) {

      ObjectMapper objectMapper = new ObjectMapper();
      try {

        response.setStatus(ex.getErrorCode().getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(ex.getErrorCode().getHttpStatus().value())
            .code(ex.getErrorCode().name())
            .message(ex.getDescription()).build();

        Object y = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

      } catch (Exception e) {

        response.setStatus(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value())
            .code(ErrorCode.INTERNAL_SERVER_ERROR.name())
            .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
            .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));


      }


    }


  }
}
