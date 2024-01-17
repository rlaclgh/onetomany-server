package com.rlaclgh.onetomany.chat_room;

import com.rlaclgh.onetomany.dto.ChannelDto;
import com.rlaclgh.onetomany.dto.ChatRoomDto;
import com.rlaclgh.onetomany.dto.CreateChatRoomDto;
import com.rlaclgh.onetomany.dto.UpdateChatRoomDto;
import com.rlaclgh.onetomany.entity.Channel;
import com.rlaclgh.onetomany.entity.ChatRoom;
import com.rlaclgh.onetomany.entity.Member;
import com.rlaclgh.onetomany.repository.ChannelRepository;
import com.rlaclgh.onetomany.repository.ChatRoomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatRoomService {


  @Autowired
  private ChatRoomRepository chatRoomRepository;

  @Autowired
  private ChannelRepository channelRepository;


  @PersistenceContext
  private EntityManager entityManager;


  public ChannelDto createChatRoom(Member member,
      CreateChatRoomDto createChatRoomDto
  ) {

    ChatRoom chatRoom = chatRoomRepository.save(
        new ChatRoom(createChatRoomDto.getName(), createChatRoomDto.getImageUrl(),
            createChatRoomDto.getDescription(), member));

    Channel channel = channelRepository.save(
        new Channel(true, chatRoom, member));

    return new ChannelDto(channel.getId(), channel.getIsHost(), new ChatRoomDto(
        channel.getChatRoom().getId(),
        channel.getChatRoom().getName(),
        channel.getChatRoom().getImageUrl(),
        channel.getChatRoom().getDescription()
    ));


  }


  public ChannelDto subscribeChatRoom(Member member, Long chatRoomId)
      throws BadRequestException, NotFoundException {

    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(NotFoundException::new);

    if (chatRoom.getOwner().getId().equals(member.getId())) {
      throw new BadRequestException("본인 채팅방을 구독할 수 없습니다.");
    }

    Channel channel1 = channelRepository.findByOwnerIdAndChatRoomId(member.getId(),
        chatRoom.getId());

    if (channel1 != null) {
      throw new BadRequestException("이미 구독한 채팅방입니다.");
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
  public ChannelDto updateChatRoom(Member member, Long chatRoomId,
      UpdateChatRoomDto updateChatRoomDto)
      throws NotFoundException {

    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(NotFoundException::new);

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

    return new ChannelDto(channel.getId(), channel.getIsHost(), new ChatRoomDto(
        channel.getChatRoom().getId(),
        channel.getChatRoom().getName(),
        channel.getChatRoom().getImageUrl(),
        channel.getChatRoom().getDescription()
    ));


  }


  public List<ChannelDto> getChatRooms() {
    List<ChannelDto> chatRooms = chatRoomRepository.findChatRooms();
    return chatRooms;
  }
}
