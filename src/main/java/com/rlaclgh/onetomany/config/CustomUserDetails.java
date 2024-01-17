package com.rlaclgh.onetomany.config;

import com.rlaclgh.onetomany.entity.Member;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
public class CustomUserDetails implements UserDetails {


  private Member member;
  private List<GrantedAuthority> authorities;

  //  @JsonIgnore
  private String password;

  public CustomUserDetails(Member member) {
    this.member = member;
  }


  public CustomUserDetails(Member member, List<GrantedAuthority> authorities) {
    this.member = member;
    this.authorities = authorities;
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return member.getPassword();
  }

  @Override
  public String getUsername() {
    return member.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
