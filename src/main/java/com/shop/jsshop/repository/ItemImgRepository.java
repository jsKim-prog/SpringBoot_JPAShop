package com.shop.jsshop.repository;

import com.shop.jsshop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    //이미지 리스트 출력
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
