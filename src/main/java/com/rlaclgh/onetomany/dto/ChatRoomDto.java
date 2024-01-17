package com.rlaclgh.onetomany.dto;

import lombok.Data;

@Data
public class ChatRoomDto {

  public ChatRoomDto(Long id, String name, String imageUrl, String description) {
    this.id = id;
    this.name = name;
    this.imageUrl = imageUrl;
    this.description = description;
  }

  private Long id;
  private String name;
  private String imageUrl;
  private String description;

}
