package com.rlaclgh.onetomany.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.rlaclgh.onetomany.entity.ChatRoom;
import com.rlaclgh.onetomany.entity.Tag;
import java.util.List;
import lombok.Data;

@Data
public class ChatRoomDto {

  public ChatRoomDto(Long id, String name, String imageUrl, String description) {
    this.id = id;
    this.name = name;
    this.imageUrl = imageUrl;
    this.description = description;
  }


  @QueryProjection
  public ChatRoomDto(ChatRoom chatRoom,
      List<Tag> tags) {
    this.id = chatRoom.getId();
    this.name = chatRoom.getName();
    this.imageUrl = chatRoom.getImageUrl();
    this.description = chatRoom.getDescription();
    this.tags = tags.stream().map((tag) -> new TagDto(tag)).toList();
  }

  private Long id;
  private String name;
  private String imageUrl;
  private String description;
  private List<TagDto> tags;

}
