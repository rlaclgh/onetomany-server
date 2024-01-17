package com.rlaclgh.onetomany.dto;

import lombok.Data;


@Data
public class UpdateChatRoomDto {

  public UpdateChatRoomDto(String name, String imageUrl, String description) {
    this.name = name;
    this.imageUrl = imageUrl;
    this.description = description;
  }

  private String name;

  private String imageUrl;
  
  private String description;

}
