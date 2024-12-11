package com.shop.jsshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDTO { //장바구니 -> 조회페이지
    private Long cartItemId; //장바구니 상품 아이디
    private String itemName; //상품명(ItemFormDTO)
    private int price; //가격(ItemFormDTO)
    private int count; //수량(CartItemDTO)
    private String imgUrl; //상품이미지 경로( ItemImgDTO)

    //생성자
    public CartDetailDTO(Long cartItemId, String itemName, int price, int count, String imgUrl){
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.price = price;
        this.count = count;
        this.imgUrl=imgUrl;
    }
}
