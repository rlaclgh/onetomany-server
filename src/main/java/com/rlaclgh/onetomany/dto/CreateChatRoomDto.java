package com.rlaclgh.onetomany.dto;


import lombok.Data;
import lombok.NonNull;

@Data
public class CreateChatRoomDto {

  @NonNull
  private String name;

  @NonNull
  private String imageUrl;

  @NonNull
  private String description;
}
