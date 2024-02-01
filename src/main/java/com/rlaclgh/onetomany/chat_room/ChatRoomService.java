package com.rlaclgh.onetomany.chat_room;

import com.rlaclgh.onetomany.constant.ErrorCode;
import com.rlaclgh.onetomany.dto.ChannelDto;
import com.rlaclgh.onetomany.dto.ChatRoomDto;
import com.rlaclgh.onetomany.dto.CreateChatRoomDto;
import com.rlaclgh.onetomany.dto.UpdateChatRoomDto;
import com.rlaclgh.onetomany.entity.Channel;
import com.rlaclgh.onetomany.entity.ChatRoom;
import com.rlaclgh.onetomany.entity.ChatRoomTag;
import com.rlaclgh.onetomany.entity.Member;
import com.rlaclgh.onetomany.exception.CustomException;
import com.rlaclgh.onetomany.repository.ChannelRepository;
import com.rlaclgh.onetomany.repository.ChatRoomRepository;
import com.rlaclgh.onetomany.repository.ChatRoomTagRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatRoomService {


  @Autowired
  private ChatRoomRepository chatRoomRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private ChatRoomTagRepository chatRoomTagRepository;


  public ChatRoomDto createChatRoom(Member member,
      CreateChatRoomDto createChatRoomDto
  ) {

    ChatRoom chatRoom = chatRoomRepository.save(
        new ChatRoom(createChatRoomDto.getName(), createChatRoomDto.getImageUrl(),
            createChatRoomDto.getDescription(), member));

    long[] tagIds = createChatRoomDto.getTagIds();
    if (tagIds != null) {
      for (long tagId : tagIds) {
        chatRoomTagRepository.save(new ChatRoomTag(chatRoom, tagId));
      }

    }

    Channel channel = channelRepository.save(
        new Channel(true, chatRoom, member));

    return new ChatRoomDto(
        channel.getChatRoom().getId(),
        channel.getChatRoom().getName(),
        channel.getChatRoom().getImageUrl(),
        channel.getChatRoom().getDescription()
    );


  }


  public ChannelDto subscribeChatRoom(Member member, Long chatRoomId) {

    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "존재하지 않은 채팅방입니다."));

    if (chatRoom.getOwner().getId().equals(member.getId())) {
      throw new CustomException(ErrorCode.BAD_REQUEST, "자신의 채팅방은 구독할 수 없습니다.");
    }

    Channel channel1 = channelRepository.findByOwnerIdAndChatRoomId(member.getId(),
        chatRoom.getId());

    if (channel1 != null) {
      throw new CustomException(ErrorCode.BAD_REQUEST, "이미 구독한 채팅방입니다.");
    }

    Channel channel = channelRepository.save(
        new Channel(false, chatRoom, member));

    return new ChannelDto(channel.getId(), channel.getIsHost(), new ChatRoomDto(
        channel.getChatRoom().getId(),
        channel.getChatRoom().getName(),
        channel.getChatRoom().getImageUrl(),
        channel.getChatRoom().getDescription()
    ));
  }


  @Transactional
  public ChatRoomDto updateChatRoom(Member member, Long chatRoomId,
      UpdateChatRoomDto updateChatRoomDto) {

    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "존재하지 않은 채팅방입니다."));

    if (updateChatRoomDto.getName() != null) {
      chatRoom.setName(updateChatRoomDto.getName());
    }

    if (updateChatRoomDto.getDescription() != null) {
      chatRoom.setDescription(updateChatRoomDto.getDescription());
    }

    if (updateChatRoomDto.getImageUrl() != null) {
      chatRoom.setImageUrl(updateChatRoomDto.getImageUrl());
    }

    Channel channel = channelRepository.findByOwnerIdAndChatRoomId(member.getId(),
        chatRoom.getId());

    return new ChatRoomDto(
        channel.getChatRoom().getId(),
        channel.getChatRoom().getName(),
        channel.getChatRoom().getImageUrl(),
        channel.getChatRoom().getDescription()
    );


  }


  public List<ChatRoomDto> getChatRooms() {
    return chatRoomRepository.findChatRooms();
  }


  public ChatRoomDto getChatRoom(Long chatRoomId) {
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "존재하지 않은 채팅방입니다."));

    return new ChatRoomDto(
        chatRoom.getId(),
        chatRoom.getName(),
        chatRoom.getImageUrl(),
        chatRoom.getDescription()
    );


  }
}
