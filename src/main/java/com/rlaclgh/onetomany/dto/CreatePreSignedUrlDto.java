package com.rlaclgh.onetomany.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreatePreSignedUrlDto {

  @NotNull
  private String contentType;

}
