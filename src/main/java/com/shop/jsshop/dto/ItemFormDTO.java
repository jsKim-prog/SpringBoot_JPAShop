package com.shop.jsshop.dto;

import com.shop.jsshop.constant.ItemSellStatus;
import com.shop.jsshop.entity.Item;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDTO { //상품데이터 정보 전달용
    private Long id;
    @NotEmpty(message = "상품명은 필수 입력값 입니다.")
    private String itemName;
    @NotNull(message = "가격은 필수 입력값 입니다.")
    private int price;
    @NotNull(message = "재고는 필수 입력값 입니다.")
    private int stockNumber;
    @NotEmpty(message = "상품설명은 필수 입력값 입니다.")
    private String itemDetail;
    private ItemSellStatus itemSellStatus;

    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();


    public Item createItem(){ //dto to entity
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDTO of(Item item){ //entity to dto
        return modelMapper.map(item, ItemFormDTO.class);
    }

}
