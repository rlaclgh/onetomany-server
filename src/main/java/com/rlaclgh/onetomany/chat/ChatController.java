package com.rlaclgh.onetomany.chat;


import com.rlaclgh.onetomany.dto.ChatDto;
import com.rlaclgh.onetomany.dto.GetChatsDto;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {


  @Autowired
  private ChatService chatService;

  private final SimpMessagingTemplate template;

  @Autowired
  public ChatController(SimpMessagingTemplate template) {
    this.template = template;
  }

  @MessageMapping("chat.{channelId}")
  public void sendChat(@Payload String payload,
      Principal principal,
      @DestinationVariable Long channelId) throws ParseException, IOException {

    chatService.sendChat(principal, channelId, payload);
  }


  @GetMapping("")
  public List<ChatDto> getChats(GetChatsDto getChatsDto) {

    return chatService.getChats(getChatsDto);
  }


}
