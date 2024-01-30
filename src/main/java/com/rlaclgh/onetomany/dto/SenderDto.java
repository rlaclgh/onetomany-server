package com.rlaclgh.onetomany.dto;

import lombok.Data;

@Data
public class SenderDto {


  private Long id;
  private String nickname;

  public SenderDto(Long id, String nickname) {
    this.id = id;
    this.nickname = nickname;
  }

}
