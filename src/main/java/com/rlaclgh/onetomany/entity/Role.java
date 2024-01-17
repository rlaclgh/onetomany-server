package com.rlaclgh.onetomany.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
public class Role {

  public Role(int id, String name) {
    this.id = id;
    this.name = name;
  }

  @Id
  private int id;
  private String name;

  @OneToMany(mappedBy = "role")
  List<Member> members = new ArrayList<>();

  @OneToMany(mappedBy = "role")
  List<Authority> authorities = new ArrayList<>();

}
