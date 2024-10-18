package com.shop.jsshop.repository;

import com.shop.jsshop.dto.ItemSearchDTO;
import com.shop.jsshop.dto.MainItemDTO;
import com.shop.jsshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom { //사용자정의 리포지토리(p.267) - ItemRepository 에서 상속받음
    Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable); //관리자용 상품리스트
    Page<MainItemDTO> getMainItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable); //사용자용 상품리스트
}
