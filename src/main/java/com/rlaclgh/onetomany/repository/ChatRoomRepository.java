package com.rlaclgh.onetomany.repository;

import com.rlaclgh.onetomany.entity.ChatRoom;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>,
    ChatRoomRepositoryCustom {


  Optional<ChatRoom> findById(Long id);


  @EntityGraph(attributePaths = {"owner"})
  @Query(value = "select c, m from ChatRoom c left join c.owner m")
  Page<Object> findChatRoomsBy(PageRequest pageRequest);


}
