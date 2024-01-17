package com.rlaclgh.onetomany.auth;

import com.rlaclgh.onetomany.config.CustomUserDetails;
import com.rlaclgh.onetomany.config.CustomUserDetailsService;
import com.rlaclgh.onetomany.constant.ErrorCode;
import com.rlaclgh.onetomany.constant.RoleEnum;
import com.rlaclgh.onetomany.dto.SignInDto;
import com.rlaclgh.onetomany.dto.SignUpDto;
import com.rlaclgh.onetomany.entity.Member;
import com.rlaclgh.onetomany.exception.CustomException;
import com.rlaclgh.onetomany.repository.MemberRepository;
import com.rlaclgh.onetomany.repository.RoleRepository;
import com.rlaclgh.onetomany.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {


  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private JwtUtil jwtUtil;


  @Autowired
  private MemberRepository memberRepository;


  public void signUp(SignUpDto signUpDto) throws DataIntegrityViolationException {

    Member member = new Member(
        signUpDto.getEmail(),
        passwordEncoder.encode(signUpDto.getPassword())
    );

    member.setRole(roleRepository.getReferenceById(RoleEnum.USER.getId()));

    try {
      memberRepository.save(member);
    } catch (DataIntegrityViolationException ex) {
      throw new CustomException(ErrorCode.BAD_REQUEST, "중복된 이메일입니다.");
    }
  }

  public String signIn(SignInDto signInDto) {
    // Password Validation

    CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(
        signInDto.getEmail());

    if (passwordEncoder.matches(signInDto.getPassword(), userDetails.getPassword())) {

      String token = jwtUtil.generateToken(userDetails);

      return token;

    } else {
      throw new CustomException(ErrorCode.BAD_REQUEST, "비밀번호가 틀렸습니다.");
    }


  }


}
