package com.shop.jsshop.service;

import com.shop.jsshop.dto.CartDetailDTO;
import com.shop.jsshop.dto.CartItemDTO;
import com.shop.jsshop.dto.CartOrderDTO;
import com.shop.jsshop.dto.OrderDTO;
import com.shop.jsshop.entity.*;
import com.shop.jsshop.repository.CartItemRepository;
import com.shop.jsshop.repository.CartRepository;
import com.shop.jsshop.repository.ItemRepository;
import com.shop.jsshop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    //장바구니 담기(추가)
    public Long addCart(CartItemDTO cartItemDTO, String email) {
        //상품(item), member entity 조회
        //로그인 회원의 장바구니(cart) 엔티티 조회-> 없으면 생성
        //cart에 현재 상품(cartItem)이 있으면 추가, 없으면 엔티티 생성
        //cartItem (상품) 저장 후 id 리턴
        Item item = itemRepository.findById(cartItemDTO.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart); //cart 생성
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDTO.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDTO.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }

    }

    //장바구니 리스트(p.343)
    @Transactional(readOnly = true)
    public List<CartDetailDTO> getCartList(String email) {

        List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();
        //member 정보로 장바구니 조회
        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            return cartDetailDTOList;
        }
        //장바구니 id로 리스트 조회, 리턴
        //List<CartDetailDTO> findCartDetailDtoList(Long cartId);
        cartDetailDTOList = cartItemRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDTOList;
    }

    //장바구니 업데이트(p.352)
    //회원정보 검증 후 수량 업데이트
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        Member curMember = memberRepository.findByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();
        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            //다른 계정은 false
            return false;
        }
        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }

    //장바구니 상품삭제(p.356)
    public void deleteCartItem(Long cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    //장바구니 상품 주문(p.362)
    //장바구니페이지(주문상품번호) -> orderDTO(order service 이용 위해)
    //주문서비스 호출 후 완료되면 장바구니에서 삭제
    public Long orderCartItem(List<CartOrderDTO> cartOrderDTOList, String email){
        List<OrderDTO> orderDTOList = new ArrayList<>(); //orderservice 이용 위한 준비
        for (CartOrderDTO cartOrderDTO:cartOrderDTOList){
            //cartorder(dto) -> order(dto)
            log.info("Service.orderCartItem/CartItemId : "+ cartOrderDTO.getCartItemId());
            CartItem cartItem = cartItemRepository.findById(cartOrderDTO.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setItemId(cartItem.getItem().getId()); //**10/20 수정!
            orderDTO.setCount(cartItem.getCount());
            orderDTOList.add(orderDTO);
        }
        Long orderId = orderService.orders(orderDTOList, email);
        for (CartOrderDTO cartOrderDTO:cartOrderDTOList){
            //장바구니에서 삭제
            CartItem cartItem = cartItemRepository.findById(cartOrderDTO.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
        return orderId;
    }

}
