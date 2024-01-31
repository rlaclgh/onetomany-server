package com.rlaclgh.onetomany.tag;


import com.rlaclgh.onetomany.dto.CreateTagDto;
import com.rlaclgh.onetomany.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tag")
public class TagController {


  @Autowired
  private TagService tagService;


  @PostMapping("")
  public ResponseEntity<TagDto> createOrFindTag(@RequestBody CreateTagDto createTagDto) {
    TagDto tag = tagService.createOrFindTag(createTagDto.getName());

    return ResponseEntity.ok(tag);
  }
}
