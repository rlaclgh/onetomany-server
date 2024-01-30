package com.rlaclgh.onetomany.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id"})
public class Chat extends BaseEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Column
  private String message;

  @Column(name = "image_url")
  private String imageUrl;


  @Column(name = "is_read")
  private Boolean isRead;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id")
  private Member sender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id")
  private Channel channel;


  public Chat(String message, String imageUrl, Member sender, Channel channel) {
    this.message = message;
    this.imageUrl = imageUrl;
    this.sender = sender;
    this.channel = channel;
    this.isRead = false;
  }
}
