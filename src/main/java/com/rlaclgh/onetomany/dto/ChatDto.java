package com.rlaclgh.onetomany.dto;


import com.rlaclgh.onetomany.entity.Chat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ChatDto {

  public ChatDto(Long id, String message, String imageUrl) {
    this.id = id;
    this.message = message;
    this.imageUrl = imageUrl;
  }

  private Long id;
  private String message;
  private String imageUrl;
  private LocalDateTime createdAt;

  public ChatDto(Chat chat) {
    this.id = chat.getId();
    this.message = chat.getMessage();
    this.imageUrl = chat.getImageUrl();
    this.createdAt = chat.getCreatedAt();
  }


}
