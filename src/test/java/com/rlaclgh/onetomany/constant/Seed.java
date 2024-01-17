package com.rlaclgh.onetomany.constant;

import com.rlaclgh.onetomany.config.CustomUserDetails;
import com.rlaclgh.onetomany.entity.Member;

public class Seed {

  public static final Member user1 = new Member(1L, "rlaclgh11@gmail.com", "password11");
  public static final Member user2 = new Member(2L, "rlaclgh22@gmail.com", "password11");


  public static final CustomUserDetails user1Details = new CustomUserDetails(
      new Member(1L, "rlaclgh11@gmail.com", "password11"));
  public static final CustomUserDetails user2Details = new CustomUserDetails(
      new Member(2L, "rlaclgh22@gmail.com", "password11"));


}
