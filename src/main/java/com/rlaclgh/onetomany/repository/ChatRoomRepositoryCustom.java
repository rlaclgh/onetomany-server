package com.rlaclgh.onetomany.repository;

import com.rlaclgh.onetomany.dto.ChannelDto;
import java.util.List;

public interface ChatRoomRepositoryCustom {

  List<ChannelDto> findChatRooms();

}
