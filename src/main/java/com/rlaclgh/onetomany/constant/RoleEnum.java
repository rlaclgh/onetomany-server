package com.rlaclgh.onetomany.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {

  USER(1),
  ADMIN(2);

  private final Integer id;
}
