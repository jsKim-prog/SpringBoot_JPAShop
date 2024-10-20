package com.shop.jsshop.entity;

import com.shop.jsshop.constant.Role;
import com.shop.jsshop.dto.MemberFormDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mariadb로 설정
    private Long id; //회원번호

    private String name; //성명

    @Column(unique = true) //중복금지
    private String email; //이메일

    private String password; //패스워드

    private String address; //주소
    
    @Enumerated(EnumType.STRING)
    private Role role; //사용자권한(사용자, admin)
    
    //메서드
    //회원생성 메서드(Member Entity 생성)--memberDTO와 passwordencoder를 받아 정보+암호화된 pw 저장
    public Member createMember(MemberFormDTO mdto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(mdto.getName());
        member.setEmail(mdto.getEmail());
        member.setAddress(mdto.getAddress());
        String pw = passwordEncoder.encode(mdto.getPassword()); //pw 암호화
        member.setPassword(pw);
        member.setRole(Role.USER);
        return member;
    }


}
