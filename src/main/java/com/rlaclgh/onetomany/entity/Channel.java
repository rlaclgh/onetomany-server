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
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id"})
public class Channel extends BaseEntity {

  public Channel(Boolean isHost, ChatRoom chatRoom, Member owner) {
    this.isHost = isHost;
    this.chatRoom = chatRoom;
    this.owner = owner;
  }


  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "is_host")
  private Boolean isHost;


  //  @JsonIgnore
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "chat_room_id")
  private ChatRoom chatRoom;


  @JsonIgnore
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "owner_id")
  private Member owner;


  @JsonIgnore
  @OneToMany(mappedBy = "channel")
  List<Chat> chats = new ArrayList<>();

}
