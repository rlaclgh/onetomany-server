package com.rlaclgh.onetomany.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTagDto {

  private String name;

  public CreateTagDto(String name) {
    this.name = name;
  }
}
