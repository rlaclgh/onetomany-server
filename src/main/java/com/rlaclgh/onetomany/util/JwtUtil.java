package com.rlaclgh.onetomany.util;

import com.rlaclgh.onetomany.config.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtUtil {

//  private static final String JWT_KEY = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4";


  @Value("${jwt-key}")
  private String JWT_KEY;

  @Value("${jwt-header}")
  public String JWT_HEADER = "Authorization";

  @Value("${jwt-issuer}")
  public String JWT_ISSUER = "https://onetomany.io";

  @Value("${jwt-subject}")
  public String JWT_SUBJECT = "JWT TOKEN";

  @Value("${jwt-expires-in}")
  public Integer JWT_EXPIRES_IN = 1000 * 60 * 60 * 60;

//  private final SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes());


  public String getTokenFromHeader(HttpServletRequest request) {

    String authorization = request.getHeader(JWT_HEADER);

    if (authorization != null) {
      return request.getHeader(JWT_HEADER).split(" ")[1];
    }

    return null;
  }

  public Claims getClaimsFromToken(String token) {
    return Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(JWT_KEY.getBytes()))
        .build()
        .parseSignedClaims(token).getPayload();
  }


  public String generateToken(CustomUserDetails customUserDetails) {

    return Jwts.builder().issuer(JWT_ISSUER).subject(JWT_SUBJECT)
        .claim("email", customUserDetails.getUsername()).issuedAt(new Date())
        .expiration(new Date((new Date()).getTime() + JWT_EXPIRES_IN))
        .signWith(Keys.hmacShaKeyFor(JWT_KEY.getBytes())).compact();


  }


}
