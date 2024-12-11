package com.shop.jsshop.service;

import com.shop.jsshop.dto.ItemFormDTO;
import com.shop.jsshop.dto.ItemImgDTO;
import com.shop.jsshop.dto.ItemSearchDTO;
import com.shop.jsshop.dto.MainItemDTO;
import com.shop.jsshop.entity.Item;
import com.shop.jsshop.entity.ItemImg;
import com.shop.jsshop.repository.ItemImgRepository;
import com.shop.jsshop.repository.ItemRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    //상품수정(p.256)-상품정보불러오기
    @Transactional(readOnly = true) //readOnly = true : jpa가 변경감지(더티체킹) 수행 안함-> 성능향상
    public ItemFormDTO getItemDetail(Long itemId){
        //이미지 가져오기(EN list -> Dto List)
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
        for (ItemImg itemImg: itemImgList){
            ItemImgDTO itemImgDTO = ItemImgDTO.of(itemImg); //.of() : entity To Dto
            itemImgDTOList.add(itemImgDTO);
        }
        //상품정보(item) 가져오기(EN -> Dto)
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDTO itemFormDTO  = ItemFormDTO.of(item);
        itemFormDTO.setItemImgDTOList(itemImgDTOList); //ItemFormDTO 에 정보+ 이미지리스트 세팅
        return itemFormDTO;
    }

    //상품수정(p.261)-상품정보변경 :변경감지 사용
    public Long updateItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList) throws Exception{
        //상품수정(En 가져오기->DTO)
        Item item = itemRepository.findById(itemFormDTO.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDTO); // 정보변경 메서드 실행
        //이미지 처리(기존이미지 삭제-> 새이미지 변경저장)
        List<Long> itemImgIds = itemFormDTO.getItemImgIds(); //img id list
        for (int i=0; i<itemImgFileList.size();i++){
            itemImgService.updateItemImag(itemImgIds.get(i),itemImgFileList.get(i));
            //public void updateItemImag(Long itemImgId, MultipartFile itemImgFile)
        }
        return item.getId();
    }

    //상품데이터 조회(+페이징, 검색어) : admin
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDTO, pageable);
    }

    //상품데이터 조회(+페이징, 검색어) : user(p.284)
    @Transactional(readOnly = true)
    public Page<MainItemDTO> getMainItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDTO, pageable);
    }
}
