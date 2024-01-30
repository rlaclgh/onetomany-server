package com.rlaclgh.onetomany.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "description"})
public class Feedback extends BaseEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Column()
  private String description;


  public Feedback(String description) {
    this.description = description;
  }
}
