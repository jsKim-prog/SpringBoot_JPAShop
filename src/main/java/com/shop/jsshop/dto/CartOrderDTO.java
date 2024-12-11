package com.shop.jsshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDTO { //장바구니페이지 -> 주문상품데이터 전달(p.360)
    private Long cartItemId; //장바구니 상품 아이디(CartDetailDTO)
    private List<CartOrderDTO> cartOrderDTOList; //자기자신을 리스트로 가지고 있음
}
