package com.rlaclgh.onetomany.channel;


import com.rlaclgh.onetomany.config.CurrentUser;
import com.rlaclgh.onetomany.config.CustomUserDetails;
import com.rlaclgh.onetomany.dto.ChannelDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/channel")
@Slf4j
public class ChannelController {

  private final SimpMessagingTemplate template;


  @Autowired
  private ChannelService channelService;

  @Autowired
  public ChannelController(SimpMessagingTemplate template) {
    this.template = template;
  }

  @MessageMapping("/channel")
  public void test() {
    log.info("test!!!!!");
    template.convertAndSend("/channel", "test");
  }

  @GetMapping("")
  public ResponseEntity<List<ChannelDto>> getChannels(
      @CurrentUser() CustomUserDetails currentUser
  ) {

    List<ChannelDto> channels = channelService.getChannels(currentUser.getMember());

    return ResponseEntity.ok(channels);


  }


}
