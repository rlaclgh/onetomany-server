package com.rlaclgh.onetomany.dto;


import java.time.LocalDateTime;
import java.util.Random;
import lombok.Data;

@Data
public class MessageDto {

  private Long id;
  private String message;
  private LocalDateTime createdAt;
  private Long senderId;

  public MessageDto(String message, Long senderId) {
    this.id = new Random().nextLong();
    this.message = message;
    this.senderId = senderId;
    this.createdAt = LocalDateTime.now();
  }
}
