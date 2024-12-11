package com.shop.jsshop.dto;

import com.shop.jsshop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDTO { //상품관리 클래스(p.266)
    private String searchDateType; //상품데이터 시간관련 조회(vs. 현재시간):(all, 1d, 1w, 1m, 6m)

    private ItemSellStatus searchSellStatus; //판매상태 기준 조회

    private String searchBy; //조회 유형 선택(itemName : 상품명, createdBy : 상품등록자 아이디)

    private String searchQuery = ""; //조회할 검색어 저장

}
