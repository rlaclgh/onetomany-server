package com.rlaclgh.onetomany.filter;

import com.rlaclgh.onetomany.config.CustomUserDetails;
import com.rlaclgh.onetomany.config.CustomUserDetailsService;
import com.rlaclgh.onetomany.constant.ErrorCode;
import com.rlaclgh.onetomany.exception.CustomException;
import com.rlaclgh.onetomany.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTValidatorFilter extends OncePerRequestFilter {


  static Set<String> postMethodSet = new HashSet<>(Arrays.asList(
      "/auth/sign-in", "/auth/sign-up"
  ));

  static Set<String> getMethodSet = new HashSet<>(Arrays.asList(
      "/chat_room", "/", ""
  ));


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(
        request.getServletContext());

    CustomUserDetailsService customUserDetailsService = ctx.getBean(
        CustomUserDetailsService.class);

    JwtUtil jwtUtil = ctx.getBean(JwtUtil.class);

    String token = jwtUtil.getTokenFromHeader(request);
    if (token != null) {

      try {
        Claims claims = jwtUtil.getClaimsFromToken(token);

        String email = String.valueOf(claims.get("email"));

        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);


      } catch (Exception e) {
        throw new CustomException(ErrorCode.UNAUTHORIZED, "로그인 후 이용해주세요.");

      }


    }

    filterChain.doFilter(request, response);

  }


  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {

    String path = request.getRequestURI().substring(request.getContextPath().length());
    String method = request.getMethod();

    System.out.println(request.getRequestURI());

    if (Objects.equals(method, "POST")) {
      return postMethodSet.contains(path);
    } else if (Objects.equals(method, "GET")) {

      String regexPattern = "/chat_room/\\d+";
      return getMethodSet.contains(path) || Pattern.matches(regexPattern, path);
    }
    return false;
  }
}
