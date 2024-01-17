package com.rlaclgh.onetomany.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
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

      response.setStatus(ex.getErrorCode().getHttpStatus().value());
      response.setContentType("application/json;charset=UTF-8");

      ErrorResponse errorResponse = ErrorResponse.builder()
          .status(ex.getErrorCode().getHttpStatus().value())
          .code(ex.getErrorCode().name())
          .message(ex.getDescription()).build();

      Object y = objectMapper.writeValueAsString(errorResponse);

      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

    }


  }
}
