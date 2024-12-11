package com.shop.jsshop.repository;

import com.shop.jsshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    //cart entity CRUD
    //로그인 회원의 Cart entity 검색
    Cart findByMemberId(Long memberId);
}
