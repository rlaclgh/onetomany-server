package com.rlaclgh.onetomany.repository;

import com.rlaclgh.onetomany.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {


  Chat findFirstByChannelIdOrderByCreatedAtDesc(Long channelId);


}
