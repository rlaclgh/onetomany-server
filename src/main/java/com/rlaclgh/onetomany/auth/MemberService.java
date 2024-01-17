package com.rlaclgh.onetomany.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


//@Service
public class MemberService implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    System.out.println(email);

    return new User("das", "ads", null);
  }
}
