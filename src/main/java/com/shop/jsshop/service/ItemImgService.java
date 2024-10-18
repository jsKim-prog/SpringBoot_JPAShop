package com.shop.jsshop.service;

import com.shop.jsshop.entity.ItemImg;
import com.shop.jsshop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    @Value("${itemImgLocation}") //import org.springframework.beans.factory.annotation.Value;
    private String itemImgLocation; //application.properties 에 등록한 이미지 파일 경로

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    //이미지 저장
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{//(EN, MultipartFile)
        String originalName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일업로드
        if(!StringUtils.isEmpty(originalName)){
            imgName = fileService.uploadFile(itemImgLocation, originalName, itemImgFile.getBytes());
            imgUrl = "/images/item/"+imgName;
        }
        //상품이미지 정보저장
        itemImg.updateItemImg(originalName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }



}
