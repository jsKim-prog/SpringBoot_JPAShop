package com.shop.jsshop.service;

import com.shop.jsshop.constant.Role;
import com.shop.jsshop.dto.MemberFormDTO;
import com.shop.jsshop.entity.Member;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Log4j2
@Transactional //테스트 후 롤백처리 위함 : 같은 메소드 반복 테스트
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberServiceTests {
    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDTO member = new MemberFormDTO();
        member.setName("테스터");
        member.setEmail("test@email.com");
        member.setAddress("수원시");
        member.setPassword("1234");
        return new Member().createMember(member, passwordEncoder);
    }

    @Test //회원가입테스트
    public void saveMemberTest() {
        Member member = createMember(); //entity 생성
        Member saveMember = memberService.saveMember(member); //db저장
        //요청값과 저장값의 비교
        assertEquals(member.getEmail(), saveMember.getEmail());
        assertEquals(member.getName(), saveMember.getName());
        assertEquals(member.getAddress(), saveMember.getAddress());
        assertEquals(member.getRole(), saveMember.getRole());
        assertEquals(member.getPassword(), saveMember.getPassword());
    }

    @Test //이메일 중복 테스트
    @DisplayName("Duplicate Email Check")
    public void duplicateMemberTest() {
        Member member1 = createMember();
        Member member2 = createMember();

        memberService.saveMember(member1);
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });

        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }

}
