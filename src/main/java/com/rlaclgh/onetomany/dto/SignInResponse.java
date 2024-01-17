package com.rlaclgh.onetomany.dto;


import lombok.Data;
import lombok.NonNull;

@Data
public class SignInResponse {

//
//  @NonNull
//  private String email;

  @NonNull
  private String token;


}
