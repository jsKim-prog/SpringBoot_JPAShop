package com.shop.jsshop.service;

import com.shop.jsshop.entity.ItemImg;
import com.shop.jsshop.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
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

    //이미지 수정(p.259 : 변경감지 기능 사용)
    public void updateItemImag(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        //저장된 파일이 없으면 예외처리
        if(!itemImgFile.isEmpty()){
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);
            //저장파일 있으면 기존 파일 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())){ //저장파일명이 존재하면
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());
            }
            //새상품 변경저장
            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); //저장명 : uuid.확장자
            String imgUrl = "/images/item/"+imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
            //public void updateItemImg(String oriImgName, String imgName, String imgUrl)
        }

    }




}
