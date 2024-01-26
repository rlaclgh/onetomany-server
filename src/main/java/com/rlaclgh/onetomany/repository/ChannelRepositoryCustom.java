package com.rlaclgh.onetomany.repository;

import com.rlaclgh.onetomany.dto.ChannelDto;
import java.util.List;

public interface ChannelRepositoryCustom {

  List<ChannelDto> getChannelsWithChatRoom(Long ownerId);

}
