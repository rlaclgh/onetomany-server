package com.rlaclgh.onetomany.repository;

import com.rlaclgh.onetomany.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {


  Tag findByName(String name);


}
