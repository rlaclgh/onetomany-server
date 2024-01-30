package com.rlaclgh.onetomany.config;

import com.rlaclgh.onetomany.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements
    WebSocketMessageBrokerConfigurer {


  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptor() {
      @Override
      public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

          String token = accessor.getFirstNativeHeader("Authorization").split(" ")[1];

          Claims claims = jwtUtil.getClaimsFromToken(token);

          String email = String.valueOf(claims.get("email"));

          CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

          Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null,
              userDetails.getAuthorities());

          SecurityContextHolder.getContext().setAuthentication(auth);

          accessor.setUser(auth);

        }
        return message;
      }
    });
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws").setAllowedOrigins("*");
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {

    config.setPathMatcher(new AntPathMatcher("."));
    config.setApplicationDestinationPrefixes("/ws");
    config.enableSimpleBroker("/topic", "/queue", "/channel");
  }

}
