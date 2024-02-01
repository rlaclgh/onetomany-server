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
@ToString(of = {"id", "name"})
public class ChatRoom extends BaseEntity {

  public ChatRoom(String name, String imageUrl, String description, Member owner) {
    this.name = name;
    this.imageUrl = imageUrl;
    this.description = description;
    this.owner = owner;
  }

  @Id
  @GeneratedValue
  private Long id;

  @Column
  private String name;

  @Column(name = "image_url")
  private String imageUrl;

  @Column
  private String description;


  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private Member owner;


  @JsonIgnore
  @OneToMany(mappedBy = "chatRoom")
  List<Channel> channels = new ArrayList<>();

  //  @JsonIgnore
  @OneToMany(mappedBy = "chatRoom")
  List<ChatRoomTag> chatRoomTags = new ArrayList<>();


  public void setName(String name) {
    this.name = name;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
