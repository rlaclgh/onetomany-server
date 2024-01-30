package com.rlaclgh.onetomany.repository;

import com.rlaclgh.onetomany.dto.ChatDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ChatRepositoryCustom {

  List<ChatDto> findChats(Long channelId, Pageable pageable);

}
