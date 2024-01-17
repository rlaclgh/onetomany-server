package com.rlaclgh.onetomany.repository;

import com.rlaclgh.onetomany.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


  Optional<Member> findByEmail(String email);

}
