package com.shop.jsshop.repository;

import com.shop.jsshop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //중복확인-이메일 검사
    Optional<Member> findByEmail(String email); //NullPointerException 방지 위해 Optional 사용

}
