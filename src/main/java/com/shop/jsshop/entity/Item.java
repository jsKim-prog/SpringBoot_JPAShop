package com.shop.jsshop.entity;

import com.shop.jsshop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_item")
@Getter
@ToString
@Setter
public class Item extends BaseEntity{
    @Id //==primary key
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO) //알아서 자동주입
    private Long id; //상품코드

    @Column(nullable = false, length = 50)
    private String itemName; //상품명

    @Column(nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob //varchar2 이상의 데이터 입력
    @Column(nullable = false)
    private String itemDetail; //상품설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품판매상태 - enum

}
