package com.shop.jsshop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDTO { //메인화면에 상품 제시용(p.280)
    private Long id; //ItemFormDTO
    private String itemName; //ItemFormDTO
    private String itemDetail; //ItemFormDTO
    private Integer price; //ItemFormDTO(int)
    private String imgUrl; //ItemImgDTO

    //생성자
    @QueryProjection  //Querudsl 로 조회시 바로 MainItemDTO 객체로 받기 -> compile :Qdomain 생성
    public MainItemDTO(Long id, String itemName, String itemDetail, String imgUrl, Integer price){
        this.id = id;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }

}
