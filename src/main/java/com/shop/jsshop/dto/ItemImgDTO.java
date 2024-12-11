package com.shop.jsshop.dto;

import com.shop.jsshop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDTO {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repimgYn; //대표이미지 여부
    //for-- entity To Dto
    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDTO of(ItemImg itemImg){ //static - 생성 없이 호출용
        return modelMapper.map(itemImg, ItemImgDTO.class); //--entity To Dto
    }


}
