package com.rlaclgh.onetomany.dto;


import lombok.Data;

@Data
public class GetChatsDto {

  private int pageParam;
  private Long channelId;


  public GetChatsDto(int pageParam, Long channelId) {
    this.pageParam = pageParam;
    this.channelId = channelId;
  }
}
