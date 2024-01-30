package com.rlaclgh.onetomany.repository;

import com.rlaclgh.onetomany.entity.Channel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long>, ChannelRepositoryCustom {


  Channel findByOwnerIdAndChatRoomId(Long chatRoomId, Long memberId);


  List<Channel> findChannelsByChatRoomId(Long chatRoomId);


  Channel findChannelByChatRoomIdAndIsHost(Long chatRoomId, Boolean isHost);


}
