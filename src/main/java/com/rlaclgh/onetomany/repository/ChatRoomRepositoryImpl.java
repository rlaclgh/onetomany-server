package com.rlaclgh.onetomany.repository;


import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rlaclgh.onetomany.dto.ChatRoomDto;
import com.rlaclgh.onetomany.dto.QChatRoomDto;
import com.rlaclgh.onetomany.entity.QChatRoom;
import com.rlaclgh.onetomany.entity.QChatRoomTag;
import com.rlaclgh.onetomany.entity.QTag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {


  @Autowired
  private JPAQueryFactory queryFactory;

  @Override
  public List<ChatRoomDto> findChatRooms() {

    QChatRoom chatRoom = QChatRoom.chatRoom;
    QChatRoomTag chatRoomTag = QChatRoomTag.chatRoomTag;
    QTag tag = QTag.tag;

    return queryFactory
        .selectFrom(chatRoom)
        .leftJoin(chatRoom.chatRoomTags, chatRoomTag)
        .leftJoin(chatRoomTag.tag, tag)
        .orderBy(
            chatRoom.createdAt.desc()
        )
        .transform(groupBy(chatRoom.id)
            .list(new QChatRoomDto(
                chatRoom, list(tag)
            ))
        );
  }

  @Override
  public ChatRoomDto findChatRoom(long chatRoomId) {

    QChatRoom chatRoom = QChatRoom.chatRoom;
    QChatRoomTag chatRoomTag = QChatRoomTag.chatRoomTag;
    QTag tag = QTag.tag;

    return queryFactory
        .selectFrom(chatRoom)
        .leftJoin(chatRoom.chatRoomTags, chatRoomTag)
        .leftJoin(chatRoomTag.tag, tag)
        .where(chatRoom.id.eq(chatRoomId))
        .transform(groupBy(chatRoom.id)
            .list(new QChatRoomDto(
                chatRoom, list(tag)
            ))
        ).stream().findFirst().orElse(null);


  }


}
