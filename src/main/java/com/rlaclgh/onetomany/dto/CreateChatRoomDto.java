package com.rlaclgh.onetomany.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NonNull;

@Data
public class CreateChatRoomDto {

  @NonNull
  @NotEmpty(message = "이름을 작성해주세요.")
  private String name;

  @NonNull
  @NotEmpty(message = "이미지를 첨부해주세요.")
  private String imageUrl;

  @NonNull
  @NotEmpty(message = "설명을 작성해주세요.")
  private String description;


}
