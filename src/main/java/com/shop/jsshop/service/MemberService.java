package com.shop.jsshop.service;

import com.shop.jsshop.entity.Member;
import com.shop.jsshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional //에러시 콜백 관리
@RequiredArgsConstructor //final, @Notnull 붙은 필드에 생성자 생성
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepo;

    //회원가입
    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepo.save(member);
    }

    //이메일 중복확인
    private void validateDuplicateMember(Member member){
        Optional<Member> checkMem = memberRepo.findByEmail(member.getEmail());
        if(checkMem.isPresent()){ //중복 이메일이 있다면
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

    }

    @Override //UserDetailsService구현, 로그인/로그아웃
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> member = memberRepo.findByEmail(email);
        if(member.isEmpty()){
            throw new UsernameNotFoundException(email);
        }
        Member loginMember = member.get();
        return User.builder().username(loginMember.getEmail()).password(loginMember.getPassword()).roles(loginMember.getRole().toString())
                .build();
    }
}
