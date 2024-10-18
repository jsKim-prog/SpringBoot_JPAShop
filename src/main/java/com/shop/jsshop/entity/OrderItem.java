package com.shop.jsshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class OrderItem extends BaseEntity{ //주문상품
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //다대일(하나의 상품<-여러 주문상품, 지연로딩방식)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY) //다대일(하나의 주문<-여러 주문상품)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문가격

    private int count; //수량


}

//Hibernate:
//        create table order_item (
//        order_item_id bigint not null auto_increment,
//        count integer not null,
//        order_price integer not null,
//        reg_time datetime(6),
//        update_time datetime(6),
//        item_id bigint,
//        order_id bigint,
//        primary key (order_item_id)
//        ) engine=InnoDB