package com.shop.jsshop.controller;

import com.shop.jsshop.dto.CartDetailDTO;
import com.shop.jsshop.dto.CartItemDTO;
import com.shop.jsshop.dto.CartOrderDTO;
import com.shop.jsshop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CartController {
    private final CartService cartService;

    //장바구니 추가
    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDTO cartItemDTO, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError: fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST); //오류메시지 재전송 BAD_REQUEST : 400
        }
        String email = principal.getName();
        Long cartItemId;
        try {
            cartItemId = cartService.addCart(cartItemDTO, email);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    //장바구니 리스트(p.344)
    @GetMapping(value = "/cart")
    public String orderHistory(Principal principal, Model model){
        List<CartDetailDTO> cartDetailDTOList = cartService.getCartList(principal.getName()); //->email
        model.addAttribute("cartItems", cartDetailDTOList);
        return "cart/cartList";
    }
    
    //장바구니 개수변경(p.353) - 일부변경이므로 @PatchMapping
    @PatchMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal){
        if (count<=0){
            return new ResponseEntity<String>("최소 1개 이상 담아주세요.", HttpStatus.BAD_REQUEST);
        } else if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    //장바구니 상품 삭제(p.357)
    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){
        if(!cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    //장바구니 상품 주문(수량 업데이트)(p.363)
    @PostMapping(value = "/cart/orders")
    public @ResponseBody ResponseEntity oderCartItem(@RequestBody CartOrderDTO cartOrderDTO, Principal principal){
        log.info("/cart/orders controller 실행+++++++++++++++++");
        List<CartOrderDTO> cartOrderDTOList = cartOrderDTO.getCartOrderDTOList();
        log.info("받은 리스트 : "+cartOrderDTOList+"/"+cartOrderDTOList.get(0).getCartItemId());

        if(cartOrderDTOList ==null|| cartOrderDTOList.size()==0){
            return new ResponseEntity<String>("주문할 상품을 선택해 주세요.", HttpStatus.FORBIDDEN);
        }
        for (CartOrderDTO cartOrder: cartOrderDTOList){
            if(!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())){
                return new ResponseEntity<String>("주문권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }//--for()
        Long orderId = cartService.orderCartItem(cartOrderDTOList, principal.getName());
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
