package com.rlaclgh.onetomany.channel;


import com.rlaclgh.onetomany.dto.ChannelDto;
import com.rlaclgh.onetomany.dto.ChatDto;
import com.rlaclgh.onetomany.entity.Chat;
import com.rlaclgh.onetomany.entity.Member;
import com.rlaclgh.onetomany.repository.ChannelRepository;
import com.rlaclgh.onetomany.repository.ChatRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChannelService {

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private ChatRepository chatRepository;


  public List<ChannelDto> getChannels(Member member) {

    List<ChannelDto> channels = channelRepository.getChannelsWithChatRoom(member.getId());

    for (ChannelDto channel : channels) {

      Chat chat = chatRepository.findFirstByChannelIdOrderByCreatedAtDesc(channel.getId());

      if (chat != null) {
        channel.setLastChat(new ChatDto(chat));
      } else {
        channel.setLastChat(null);

      }


    }

    return channels;

  }


}
