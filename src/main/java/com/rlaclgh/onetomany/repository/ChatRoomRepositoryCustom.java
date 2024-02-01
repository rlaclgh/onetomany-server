package com.rlaclgh.onetomany.repository;

import com.rlaclgh.onetomany.dto.ChatRoomDto;
import java.util.List;

public interface ChatRoomRepositoryCustom {

  List<ChatRoomDto> findChatRooms();


  ChatRoomDto findChatRoom(long chatRoomId);

}
