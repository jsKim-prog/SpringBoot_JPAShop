package com.shop.jsshop.entity;

import com.shop.jsshop.dto.MemberFormDTO;
import com.shop.jsshop.repository.CartRepository;
import com.shop.jsshop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Log4j2
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartTests {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @PersistenceContext
    EntityManager em;

    //테스트용 멤버 생성
    public Member createMember(){
        MemberFormDTO memDTO = new MemberFormDTO();
        memDTO.setEmail("test@email.com");
        memDTO.setName("홍길동");
        memDTO.setPassword("1234");
        memDTO.setAddress("서울시 금천구");

        return new Member().createMember(memDTO, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 테스트")
    public void findCartAndMemberTest(){
        Member member = createMember();
        memberRepository.save(member); //memeber 저장

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart); //cart 저장

        em.flush(); //영속성 컨택스트 -> DB 반영(transactional)
        em.clear(); //영속성 컨택스트 비우기

        Cart saveCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);
        log.info("--------장바구니 id/memberID : "+saveCart.getId()+" / "+saveCart.getMember().getId()); //--------장바구니 id/memberID : 1 / 1
        log.info("-----------원본 memberID : "+member.getId()); //-----------원본 memberID : 1

//        Hibernate:
//        insert
//                into
//        member
//                (address, email, name, password, role, member_id)
//        values
//                (?, ?, ?, ?, ?, default)
//        Hibernate:
//        insert
//                into
//        cart
//                (member_id, cart_id)
//        values
//                (?, default)
//        Hibernate:
//        select
//        c1_0.cart_id,
//                m1_0.member_id,
//                m1_0.address,
//                m1_0.email,
//                m1_0.name,
//                m1_0.password,
//                m1_0.role
//        from
//        cart c1_0
//        left join
//        member m1_0
//        on m1_0.member_id=c1_0.member_id
//        where
//        c1_0.cart_id=?
    }


}
