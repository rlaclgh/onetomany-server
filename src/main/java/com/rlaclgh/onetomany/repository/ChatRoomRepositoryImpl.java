package com.rlaclgh.onetomany.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rlaclgh.onetomany.dto.ChannelDto;
import com.rlaclgh.onetomany.dto.ChatRoomDto;
import com.rlaclgh.onetomany.entity.QChannel;
import com.rlaclgh.onetomany.entity.QChatRoom;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {


  @Autowired
  private JPAQueryFactory queryFactory;

  @Override
  public List<ChannelDto> findChatRooms() {

    QChatRoom chatRoom = QChatRoom.chatRoom;
    QChannel channel = QChannel.channel;

    List<ChannelDto> chatRooms = queryFactory
        .select(Projections.constructor(ChannelDto.class,
                channel.id, channel.isHost,
                Projections.constructor(ChatRoomDto.class,
                    chatRoom.id, chatRoom.name, chatRoom.imageUrl, chatRoom.description)
            )
        )
        .from(chatRoom)
        .leftJoin(channel)
        .on(channel.chatRoom.id.eq(chatRoom.id).and(channel.isHost.isTrue()))
        .fetchJoin()
        .fetch();

//    List<ChatRoom> chatRooms = queryFactory
//        .selectFrom(chatRoom)
//        .leftJoin(channel)
//
//        .on(channel.chatRoom.id.eq(chatRoom.id).and(channel.isHost.isTrue()))
//        .fetchJoin()
//        .fetch();

    return chatRooms;
  }
}
