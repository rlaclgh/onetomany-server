package com.rlaclgh.onetomany.channel;


import com.rlaclgh.onetomany.config.CurrentUser;
import com.rlaclgh.onetomany.config.CustomUserDetails;
import com.rlaclgh.onetomany.config.CustomUserDetailsService;
import com.rlaclgh.onetomany.config.SseEmitterManager;
import com.rlaclgh.onetomany.dto.ChannelDto;
import com.rlaclgh.onetomany.util.JwtUtil;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/channel")
@Slf4j
public class ChannelController {

  private final SimpMessagingTemplate template;


  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;


  @Autowired
  private ChannelService channelService;

  @Autowired
  private SseEmitterManager sseEmitterManager;


  @Autowired
  public ChannelController(SimpMessagingTemplate template) {
    this.template = template;
  }

  @MessageMapping("/channel")
  public void test() {
    log.info("test!!!!!");
    template.convertAndSend("/channel", "test");
  }


  @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter handle(@RequestParam String token) throws IOException {

    Claims claims = jwtUtil.getClaimsFromToken(token);

    String email = String.valueOf(claims.get("email"));

    CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);

    String userId = userDetails.getMember().getId().toString();

    SseEmitter emitter = new SseEmitter((long) (1000 * 60 * 5));

    Map<String, SseEmitter> sseEmitterMap = sseEmitterManager.getSseEmitterMap();

    sseEmitterMap.put(userId, emitter);

    emitter.onCompletion(() -> sseEmitterMap.remove(userId));
    emitter.onTimeout(() -> sseEmitterMap.remove(userId));

    emitter.send("test");

    // Save the emitter somewhere..

    return emitter;
  }

  @GetMapping("")
  public ResponseEntity<List<ChannelDto>> getChannels(
      @CurrentUser() CustomUserDetails currentUser
  ) {

    List<ChannelDto> channels = channelService.getChannels(currentUser.getMember());

    return ResponseEntity.ok(channels);


  }


}
