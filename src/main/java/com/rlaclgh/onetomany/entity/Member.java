package com.rlaclgh.onetomany.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "email"})
public class Member extends BaseEntity {

  public Member(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public Member(Long id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @Id
  @GeneratedValue
  private Long id;

  @Column(unique = true)
  private String email;

  @JsonIgnore
  private String password;


  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id")
  private Role role;

  @JsonIgnore
  @OneToMany(mappedBy = "owner")
  List<ChatRoom> chatRooms = new ArrayList<>();


  @JsonIgnore
  @OneToMany(mappedBy = "owner")
  List<Channel> channels = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "sender")
  List<Chat> chats = new ArrayList<>();


}
