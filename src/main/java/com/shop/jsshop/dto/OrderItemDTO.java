package com.shop.jsshop.dto;

import com.shop.jsshop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO { //주문데이터 DTO(p.309) - 구매이력
    private String itemName; //상품명(ItemFormDTO)
    private int count; //주문수량(OrderDTO)
    private int orderPrice; //주문가격=상품가격(EN : OderItem)
    private String imgUrl;  //상품이미지 경로(ItemImgDTO)

    //생성자
    public OrderItemDTO(OrderItem orderItem, String imgUrl){
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

}
