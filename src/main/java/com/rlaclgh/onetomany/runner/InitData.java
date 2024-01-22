package com.rlaclgh.onetomany.runner;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class InitData implements ApplicationRunner {


  @Transactional
  @Override
  public void run(ApplicationArguments args) {

//    log.info("InitData");
//
//    Role roleUser = new Role(1, "ROLE_USER");
//    Role roleAdmin = new Role(2, "ROLE_ADMIN");
//
//    Authority authority1 = new Authority(1, "READ", roleUser);
//    Authority authority2 = new Authority(2, "CREATE", roleAdmin);
//    Authority authority3 = new Authority(3, "READ", roleAdmin);
//    Authority authority4 = new Authority(4, "UPDATE", roleAdmin);
//    Authority authority5 = new Authority(5, "DELETE", roleAdmin);
//
//    roleRepository.saveAll(new ArrayList<>(Arrays.asList(roleUser, roleAdmin)));
//    authorityRepository.saveAll(
//        new ArrayList<>(Arrays.asList(authority1, authority2, authority3, authority4, authority5)));
//
//    Member member = new Member("rlaclgh11@gmail.com", passwordEncoder.encode("password11"));
//    member.setRole(
//        roleRepository.getReferenceById(RoleEnum.USER.getId()));
//
//    Member member2 = new Member("rlaclgh22@gmail.com", passwordEncoder.encode("password11"));
//    member2.setRole(
//        roleRepository.getReferenceById(RoleEnum.USER.getId()));
//
//    memberRepository.save(member);
//    memberRepository.save(member2);

  }
}
