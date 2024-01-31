package com.rlaclgh.onetomany.tag;


import com.rlaclgh.onetomany.dto.TagDto;
import com.rlaclgh.onetomany.entity.Tag;
import com.rlaclgh.onetomany.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

  @Autowired
  private TagRepository tagRepository;


  public TagDto createOrFindTag(String name) {
    Tag tag = tagRepository.findByName(name);
    if (tag == null) {
      Tag createdTag = tagRepository.save(new Tag(name));
      return new TagDto(createdTag);
    }
    return new TagDto(tag);
  }
}
