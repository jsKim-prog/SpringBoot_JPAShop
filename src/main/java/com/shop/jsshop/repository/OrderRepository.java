package com.shop.jsshop.repository;

import com.shop.jsshop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> { //주문 entity CRUD
    //주문이력 조회(리스트)
    @Query("select o from Order o where o.member.email = :email order by o.orderDate desc ")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);
    //회원 당 주문개수 조회
    @Query("select count(o) from Order o where o.member.email=:email")
    Long countOrder(@Param("email") String email);
}
