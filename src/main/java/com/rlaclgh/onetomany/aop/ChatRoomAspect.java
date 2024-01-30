package com.rlaclgh.onetomany.aop;

import com.rlaclgh.onetomany.config.CustomUserDetails;
import com.rlaclgh.onetomany.entity.ChatRoom;
import com.rlaclgh.onetomany.repository.ChatRoomRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ChatRoomAspect {


  @Autowired
  private ChatRoomRepository chatRoomRepository;

  @Before("@annotation(com.rlaclgh.onetomany.aop.MyChatRoom)")
  public void checkIfMyChatRoom(JoinPoint joinPoint) throws NotFoundException {

    Object[] args = joinPoint.getArgs();

    CustomUserDetails currentUser = null;
    Long chatRoomId = null;

    for (Object arg : args) {
      if (arg instanceof CustomUserDetails) {
        currentUser = (CustomUserDetails) arg;
      } else if (arg instanceof Long) {
        chatRoomId = (Long) arg;
      }
    }

    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(NotFoundException::new);

    if (!chatRoom.getOwner().getId().equals(currentUser.getMember().getId())) {
      throw new NotFoundException();
    }


  }
}