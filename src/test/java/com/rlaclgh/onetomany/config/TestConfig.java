package com.rlaclgh.onetomany.config;


import com.rlaclgh.onetomany.constant.RoleEnum;
import com.rlaclgh.onetomany.entity.Member;
import com.rlaclgh.onetomany.repository.MemberRepository;
import com.rlaclgh.onetomany.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class TestConfig {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private MemberRepository memberRepository;


  public void signIn() {

    String email = "rlaclgh12@gmail.com";
    String password = "password";

    Member member = new Member(email, passwordEncoder.encode(password));

    member.setRole(
        roleRepository.getReferenceById(RoleEnum.USER.getId()));

    memberRepository.save(member);


  }


}
