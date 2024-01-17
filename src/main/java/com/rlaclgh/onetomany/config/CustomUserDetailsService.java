package com.rlaclgh.onetomany.config;

import com.rlaclgh.onetomany.constant.ErrorCode;
import com.rlaclgh.onetomany.entity.Authority;
import com.rlaclgh.onetomany.entity.Member;
import com.rlaclgh.onetomany.entity.Role;
import com.rlaclgh.onetomany.exception.CustomException;
import com.rlaclgh.onetomany.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


  @Autowired
  private MemberRepository memberRepository;

  @Override
  @Transactional
  public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "이메일 정보를 확인해주세요."));

    Role role = member.getRole();
    List<Authority> roleAuthorities = role.getAuthorities();

    List<GrantedAuthority> authorities = new ArrayList<>();
    for (Authority a : roleAuthorities) {
      authorities.add(new SimpleGrantedAuthority(a.getName()));

    }

    return new CustomUserDetails(member, authorities);
  }
}
