package com.rlaclgh.onetomany.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignUpDto {

  @Email
  @NotNull
  private String email;

  @NotNull
  private String nickname;

  @NotNull
  @Pattern(regexp = "(?=.*\\d)(?=.*[a-z]).{8,}")
  private String password;

  @NotNull
  @Pattern(regexp = "(?=.*\\d)(?=.*[a-z]).{8,}")
  private String rePassword;
}
