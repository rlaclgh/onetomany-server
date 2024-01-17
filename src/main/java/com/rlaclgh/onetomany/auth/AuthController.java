package com.rlaclgh.onetomany.auth;


import com.rlaclgh.onetomany.dto.SignInDto;
import com.rlaclgh.onetomany.dto.SignInResponse;
import com.rlaclgh.onetomany.dto.SignUpDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {


  @Autowired
  private AuthService authService;

  @PostMapping("/sign-up")
  public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpDto signUpDto) {
    authService.signUp(signUpDto);
    return ResponseEntity.created(null).body(null);
  }

  @PostMapping("/sign-in")
  public ResponseEntity<SignInResponse> signIn(@RequestBody SignInDto signInDto) {
    String token = authService.signIn(signInDto);
    return ResponseEntity.ok().body(new SignInResponse(token));
  }
}
