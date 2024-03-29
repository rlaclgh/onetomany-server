package com.rlaclgh.onetomany.dto;


import com.rlaclgh.onetomany.entity.Chat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ChatDto {


  private Long id;
  private String message;
  private String imageUrl;
  private LocalDateTime createdAt;
  private SenderDto sender;

  public ChatDto(Chat chat) {
    this.id = chat.getId();
    this.message = chat.getMessage();
    this.imageUrl = chat.getImageUrl();
    this.createdAt = chat.getCreatedAt();
    this.sender = new SenderDto(chat.getSender().getId(), chat.getSender().getNickname());
  }


  public ChatDto(Long id, String message, String imageUrl, LocalDateTime createdAt,
      SenderDto sender) {
    this.id = id;
    this.message = message;
    this.imageUrl = imageUrl;
    this.createdAt = createdAt;
    this.sender = sender;
  }
}
