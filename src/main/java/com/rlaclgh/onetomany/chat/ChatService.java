package com.rlaclgh.onetomany.chat;


import com.rlaclgh.onetomany.config.SseEmitterManager;
import com.rlaclgh.onetomany.constant.ErrorCode;
import com.rlaclgh.onetomany.dto.ChatDto;
import com.rlaclgh.onetomany.dto.GetChatsDto;
import com.rlaclgh.onetomany.entity.Channel;
import com.rlaclgh.onetomany.entity.Chat;
import com.rlaclgh.onetomany.entity.Member;
import com.rlaclgh.onetomany.exception.CustomException;
import com.rlaclgh.onetomany.repository.ChannelRepository;
import com.rlaclgh.onetomany.repository.ChatRepository;
import com.rlaclgh.onetomany.repository.MemberRepository;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
public class ChatService {


  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private ChatRepository chatRepository;


  @Autowired
  private SimpMessagingTemplate template;

  @Autowired
  private SseEmitterManager sseEmitterManager;


  @Transactional
  public void sendChat(Principal principal, Long channelId, String payload)
      throws ParseException, IOException {

    String email = principal.getName();

    JSONParser parser = new JSONParser();

    JSONObject jsonObject = (JSONObject) parser.parse(payload);

    String message = jsonObject.get("message").toString();

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "이메일 정보를 확인해주세요."));

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "채널 정보를 확인해주세요."));

    Map<String, SseEmitter> sseEmitterMap = sseEmitterManager.getSseEmitterMap();

    if (!channel.getOwner().getId().equals(member.getId())) {
      throw new CustomException(ErrorCode.NOT_FOUND, "채널 정보를 확인해주세요.");
    }

    // 방장인 경우
    if (channel.getIsHost()) {

      List<Channel> channels = channelRepository.findChannelsByChatRoomId(
          channel.getChatRoom().getId());

      List<Chat> chats = channels.stream().map((c) -> new Chat(message, null, member, c)).toList();
      chatRepository.saveAll(chats);

      for (Chat chat : chats) {
        template.convertAndSend(String.format("/channel/%d", chat.getChannel().getId()),
            new ChatDto(chat));
      }

      // 방장이 아닌 경우
    } else {

      Channel targetChannel = channelRepository.findChannelByChatRoomIdAndIsHost(
          channel.getChatRoom().getId(), true);

      List<Chat> chats = List.of(
          new Chat(message, null, member, targetChannel),
          new Chat(message, null, member, channel)
      );
      chatRepository.saveAll(chats);

      for (Chat chat : chats) {
        template.convertAndSend(String.format("/channel/%d", chat.getChannel().getId()),
            new ChatDto(chat));
      }

//      SseEmitter sseEmitter = sseEmitterMap.get(targetChannel.getOwner().getId().toString());
//      sseEmitter.send(SseEmitter.event().data(message).build());

    }

    // 일반 유저인 경우

  }


  public List<ChatDto> getChats(GetChatsDto getChatsDto) {

    PageRequest pageRequest = PageRequest.of(getChatsDto.getPageParam(), 20,
        Sort.by(Direction.DESC, "createdAt"));

    List<ChatDto> chats = chatRepository.findChats(getChatsDto.getChannelId(), pageRequest);
    Collections.reverse(chats);
    return chats;

  }
}
