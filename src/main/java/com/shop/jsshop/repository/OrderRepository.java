package com.shop.jsshop.repository;

import com.shop.jsshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> { //주문 entity CRUD
}
