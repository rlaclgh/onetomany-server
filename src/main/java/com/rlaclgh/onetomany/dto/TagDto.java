package com.rlaclgh.onetomany.dto;


import com.rlaclgh.onetomany.entity.Tag;
import lombok.Data;

@Data
public class TagDto {

  private Long id;
  private String name;


  public TagDto(Tag tag) {
    this.id = tag.getId();
    this.name = tag.getName();
  }
}
