package com.rlaclgh.onetomany.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rlaclgh.onetomany.dto.ChatDto;
import com.rlaclgh.onetomany.dto.SenderDto;
import com.rlaclgh.onetomany.entity.QChat;
import com.rlaclgh.onetomany.entity.QMember;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

  @Autowired
  private JPAQueryFactory queryFactory;

  @Override
  public List<ChatDto> findChats(Long channelId, Pageable pageable) {

    QChat chat = QChat.chat;
    QMember member = QMember.member;

    return queryFactory.select(
            Projections.constructor(
                ChatDto.class,
                chat.id, chat.message, chat.imageUrl, chat.createdAt,
                Projections.constructor(
                    SenderDto.class,
                    member.id, member.nickname
                )
            )
        )
        .from(chat)
        .leftJoin(member)
        .on(chat.sender.id.eq(member.id))
        .where(chat.channel.id.eq(channelId))
        .orderBy(chat.createdAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchJoin()
        .fetch();
  }
}
