package com.rlaclgh.onetomany.dto;


import lombok.Data;

@Data
public class ChannelDto {

  public ChannelDto(Long id, Boolean isHost, ChatRoomDto chatRoom) {
    this.id = id;
    this.isHost = isHost;
    this.chatRoom = chatRoom;
  }

  private Long id;
  private Boolean isHost;
  private ChatRoomDto chatRoom;
  private Long unReadCount;

  private ChatDto lastChat;


}
