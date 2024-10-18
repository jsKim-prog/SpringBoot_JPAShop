package com.shop.jsshop.controller;

import com.shop.jsshop.dto.ItemFormDTO;
import com.shop.jsshop.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    //하나의 상품객체 출력-tes용
  /*  @GetMapping(value = "/oneitem")
    public String thymeleafExample02(Model model){

        ItemDTO dto = ItemDTO.builder().itemName("ViewTest").itemDetail("ViewTestProduct1").price(2000).regTime(LocalDateTime.now())
        .build();
        model.addAttribute("itemDTO", dto);
        return "item/item";
    } */

    //관리자용 상품등록 페이지 열기
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDTO", new ItemFormDTO());

        return "item/itemForm";
    }
    
    //관리자용 상품등록
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult, Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){
        //@Valid : DTO 의 jakarta.validation.constraints.NotEmpty; 등으로 검증완료된 객체
        //BindingResult : validation 결과 값 담은 객체

        //검증결과에 오류-> 필수값이 없으면 등록페이지로 되돌아가기
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        //상품이미지 등록여부 체크-> 이미지 없으면 등록페이지로 되돌아가기
        if(itemImgFileList.get(0).isEmpty() && itemFormDTO.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        //상품등록
        //saveItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImagFileList)
        try {
            itemService.saveItem(itemFormDTO, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품등록 실패. 시스템 관리자에게 문의하세요.");
            return "item/itemForm";
        }
        return "redirect:/"; //메인페이지로 이동(**-> 관리자용 등록페이지로 이동으로 수정예정!)
    }
}
