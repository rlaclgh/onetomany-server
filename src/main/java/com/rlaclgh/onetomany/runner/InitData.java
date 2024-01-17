package com.rlaclgh.onetomany.runner;

import com.rlaclgh.onetomany.constant.RoleEnum;
import com.rlaclgh.onetomany.entity.Authority;
import com.rlaclgh.onetomany.entity.Member;
import com.rlaclgh.onetomany.entity.Role;
import com.rlaclgh.onetomany.repository.AuthorityRepository;
import com.rlaclgh.onetomany.repository.MemberRepository;
import com.rlaclgh.onetomany.repository.RoleRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class InitData implements ApplicationRunner {


  @Autowired
  private RoleRepository roleRepository;


  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthorityRepository authorityRepository;

  @Autowired
  private MemberRepository memberRepository;


  @Transactional
  @Override
  public void run(ApplicationArguments args) throws Exception {

    log.info("InitData");

    Role roleUser = new Role(1, "ROLE_USER");
    Role roleAdmin = new Role(2, "ROLE_ADMIN");

    Authority authority1 = new Authority(1, "READ", roleUser);
    Authority authority2 = new Authority(2, "CREATE", roleAdmin);
    Authority authority3 = new Authority(3, "READ", roleAdmin);
    Authority authority4 = new Authority(4, "UPDATE", roleAdmin);
    Authority authority5 = new Authority(5, "DELETE", roleAdmin);

    roleRepository.saveAll(new ArrayList<>(Arrays.asList(roleUser, roleAdmin)));
    authorityRepository.saveAll(
        new ArrayList<>(Arrays.asList(authority1, authority2, authority3, authority4, authority5)));

    Member member = new Member("rlaclgh11@gmail.com", passwordEncoder.encode("password11"));
    member.setRole(
        roleRepository.getReferenceById(RoleEnum.USER.getId()));

    Member member2 = new Member("rlaclgh22@gmail.com", passwordEncoder.encode("password11"));
    member2.setRole(
        roleRepository.getReferenceById(RoleEnum.USER.getId()));

    memberRepository.save(member);
    memberRepository.save(member2);

  }
}
