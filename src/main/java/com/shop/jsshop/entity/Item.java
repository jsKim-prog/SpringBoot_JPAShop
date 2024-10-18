package com.shop.jsshop.entity;

import com.shop.jsshop.constant.ItemSellStatus;
import com.shop.jsshop.dto.ItemFormDTO;
import com.shop.jsshop.exception.OutOfStockException;
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

    //item 정보 변경 메서드
    public void updateItem(ItemFormDTO itemFormDTO){
        this.itemName=itemFormDTO.getItemName();
        this.price = itemFormDTO.getPrice();
        this.stockNumber = itemFormDTO.getStockNumber();
        this.itemDetail=itemFormDTO.getItemDetail();
        this.itemSellStatus=itemFormDTO.getItemSellStatus();

    }

    //재고관리 메서드 - 사용자정의 예외(OutofStockException) 사용(p.297)
    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber; //주문 후 남은 수량
        if (restStock<0){ //??0포함해야 하는 것 아님?
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고수량: "+this.stockNumber+")");
        }
        this.stockNumber = restStock;
    }


}
