package com.shop.jsshop.controller;

import com.shop.jsshop.dto.OrderDTO;
import com.shop.jsshop.dto.OrderHistoryDTO;
import com.shop.jsshop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class OrderController {
    private final OrderService orderService;

    //주문하기
    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDTO orderDTO, BindingResult bindingResult, Principal principal){
        //@ResponseBody, @RequestBody : 스프링 비동기 처리 위함
        //Principal : security 에서 넘겨주는 Member 정보
        log.info("order Controller 실행++++++++++++++++++++");

        if (bindingResult.hasErrors()){
            log.info("bad request.......order");
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError: fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST); //오류메시지 재전송

        }
        String email = principal.getName();
        Long orderId;
        try {
            orderId = orderService.order(orderDTO, email);
        }catch (Exception e){
            log.info("UNAUTHORIZED.......order");
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
            //**302 return-> BAD_REQUEST:400 /html에서 인식하는 401 (UNAUTHORIZED)로 수정
            //**컨트롤러 문제가 아니므로 다시 되돌림
        }
       return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

    //구매이력 조회(p.314)
    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHistory(@PathVariable("page")Optional<Integer> page, Principal principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 4);

        Page<OrderHistoryDTO> orderHistoryDTOList = orderService.getOrderList(principal.getName(), pageable);
       // log.info(orderHistoryDTOList.getContent()); //List<OrderHistoryDTO>

        model.addAttribute("orders", orderHistoryDTOList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHistory";
    }

    //주문취소(p.324)
    @PostMapping(value = "/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal){
        if(!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("주문취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
