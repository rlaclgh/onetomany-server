package com.rlaclgh.onetomany.repository;

import com.rlaclgh.onetomany.entity.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>,
    ChatRoomRepositoryCustom {


  Optional<ChatRoom> findById(Long id);


}
