package com.rlaclgh.onetomany.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority implements GrantedAuthority {


  public Authority(int id, String name, Role role) {
    this.id = id;
    this.name = name;
    this.role = role;
  }


  @Id
  private int id;
  private String name;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id")
  private Role role;


  @Override
  public String getAuthority() {
    return name;
  }
}
