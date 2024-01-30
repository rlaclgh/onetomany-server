package com.rlaclgh.onetomany.repository;

import com.rlaclgh.onetomany.entity.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {


  Chat findFirstByChannelIdOrderByCreatedAtDesc(Long channelId);


  long countByIsReadAndChannelId(Boolean isRead, Long channelId);


  Slice<Chat> findChatsByChannelId(Long channelId, Pageable pageable);


}
