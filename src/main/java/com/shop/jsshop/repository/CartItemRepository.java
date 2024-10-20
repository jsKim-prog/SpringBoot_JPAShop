package com.shop.jsshop.repository;

import com.shop.jsshop.dto.CartDetailDTO;
import com.shop.jsshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    //장바구니 상품 조회, 저장
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    //리스트 조회(장바구니 페이지)-p.342
    @Query("select new com.shop.jsshop.dto.CartDetailDTO(ci.id, i.itemName, i.price, ci.count, im.imgUrl) "+
    "from CartItem ci, ItemImg im join ci.item i "+
    "where ci.cart.id=:cartId "+
    "and im.item.id = ci.item.id "+
    "and im.repimgYn = 'Y' "+
    "order by ci.regTime desc ")
    List<CartDetailDTO> findCartDetailDtoList(Long cartId);
}
