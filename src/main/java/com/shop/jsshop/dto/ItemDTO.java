package com.shop.jsshop.dto;

import com.shop.jsshop.constant.ItemSellStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO { //View 전달용 객체
    private Long id;
    private String itemName;
    private int price;
    private int stockNumber;
    private String itemDetail;
    private ItemSellStatus itemSellStatus;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

}
