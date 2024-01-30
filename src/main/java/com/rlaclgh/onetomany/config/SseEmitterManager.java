package com.rlaclgh.onetomany.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitterManager {

  private final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

  public Map<String, SseEmitter> getSseEmitterMap() {
    return sseEmitterMap;
  }

}
