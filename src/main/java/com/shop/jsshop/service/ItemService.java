package com.shop.jsshop.service;

import com.shop.jsshop.dto.ItemFormDTO;
import com.shop.jsshop.entity.Item;
import com.shop.jsshop.entity.ItemImg;
import com.shop.jsshop.repository.ItemImgRepository;
import com.shop.jsshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class ItemService { //상품관리 서비스
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    //상품등록
    public Long saveItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImagFileList) throws Exception{
        //상품등록
        Item item = itemFormDTO.createItem();
        itemRepository.save(item);
        
        //이미지 등록
        for (int i=0; i<itemImagFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0){ //첫 번째 이미지인경우(대표이미지)
                itemImg.setRepimgYn("Y"); //대표이미지 세팅
            }else {
                itemImg.setRepimgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImagFileList.get(i));
            //saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) :파일업로드+상품이미지 정보저장
        } //--for()

        return item.getId();
    }
}
