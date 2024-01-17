package com.rlaclgh.onetomany.dto;

import com.rlaclgh.onetomany.entity.Channel;
import lombok.Data;
import lombok.NonNull;


@Data
public class CreateChatRoomResponse {

  @NonNull
  private Channel channel;
}
