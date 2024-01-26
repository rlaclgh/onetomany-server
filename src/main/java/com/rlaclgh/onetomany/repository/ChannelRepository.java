package com.rlaclgh.onetomany.repository;

import com.rlaclgh.onetomany.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long>, ChannelRepositoryCustom {


  Channel findByOwnerIdAndChatRoomId(Long chatRoomId, Long memberId);

}
