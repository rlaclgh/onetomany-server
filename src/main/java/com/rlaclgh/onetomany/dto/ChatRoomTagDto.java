package com.rlaclgh.onetomany.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ChatRoomTagDto {


  private Long id;


  @QueryProjection
  public ChatRoomTagDto(Long id) {
    this.id = id;
  }
}
