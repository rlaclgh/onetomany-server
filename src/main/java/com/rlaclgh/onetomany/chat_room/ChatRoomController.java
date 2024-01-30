package com.rlaclgh.onetomany.chat_room;


import com.rlaclgh.onetomany.aop.MyChatRoom;
import com.rlaclgh.onetomany.config.CurrentUser;
import com.rlaclgh.onetomany.config.CustomUserDetails;
import com.rlaclgh.onetomany.dto.ChannelDto;
import com.rlaclgh.onetomany.dto.ChatRoomDto;
import com.rlaclgh.onetomany.dto.CreateChatRoomDto;
import com.rlaclgh.onetomany.dto.UpdateChatRoomDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat_room")
@Slf4j
public class ChatRoomController {


  @Autowired
  private ChatRoomService chatRoomService;


  @PostMapping()
  public ResponseEntity<ChatRoomDto> createChatRoom(
      @RequestBody @Valid CreateChatRoomDto createChatRoomDto,
      @CurrentUser() CustomUserDetails currentUser
  ) {
    ChatRoomDto chatRoomDto = chatRoomService.createChatRoom(
        currentUser.getMember(),
        createChatRoomDto);
    return ResponseEntity.created(null).body(chatRoomDto);
  }


  @PostMapping("/{chatRoomId}/subscribe")
  public ResponseEntity<ChannelDto> subscribeChatRoom(
      @CurrentUser() CustomUserDetails currentUser,
      @PathVariable("chatRoomId") Long chatRoomId
  ) {

    ChannelDto channelDto = chatRoomService.subscribeChatRoom(
        currentUser.getMember(), chatRoomId);

    return ResponseEntity.ok(channelDto);

  }


  @MyChatRoom
  @PatchMapping("/{chatRoomId}")
  public ResponseEntity<ChatRoomDto> updateChatRoom(
      @CurrentUser() CustomUserDetails currentUser,
      @PathVariable("chatRoomId") Long chatRoomId,
      @RequestBody @Valid UpdateChatRoomDto updateChatRoomDto
  ) {

    ChatRoomDto chatRoomDto = this.chatRoomService.updateChatRoom(currentUser.getMember(),
        chatRoomId,
        updateChatRoomDto);

    return ResponseEntity.ok(chatRoomDto);

  }

  @GetMapping("")
  public ResponseEntity<List<ChannelDto>> getChatRooms() {
    List<ChannelDto> channels = chatRoomService.getChatRooms();
    return ResponseEntity.ok(channels);
  }


  @GetMapping("{chatRoomId}")
  public ResponseEntity<ChatRoomDto> getChatRoom(
      @PathVariable("chatRoomId") Long chatRoomId
  ) {
    ChatRoomDto chatRoom = chatRoomService.getChatRoom(chatRoomId);
    return ResponseEntity.ok(chatRoom);
  }


}
