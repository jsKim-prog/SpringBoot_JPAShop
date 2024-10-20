package com.shop.jsshop.service;

import com.shop.jsshop.dto.OrderDTO;
import com.shop.jsshop.dto.OrderHistoryDTO;
import com.shop.jsshop.dto.OrderItemDTO;
import com.shop.jsshop.entity.*;
import com.shop.jsshop.repository.ItemImgRepository;
import com.shop.jsshop.repository.ItemRepository;
import com.shop.jsshop.repository.MemberRepository;
import com.shop.jsshop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class OrderService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    //주문생성(p.301)
    public Long order(OrderDTO orderDTO, String email){
        Item item = itemRepository.findById(orderDTO.getItemId()).orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOderItem(item, orderDTO.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);
        return order.getId();
    }

    //주문목록 조회(p.313)
    @Transactional(readOnly = true)
    public Page<OrderHistoryDTO> getOrderList(String email, Pageable pageable){
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistoryDTO> orderHistoryDTOS = new ArrayList<>();
        for (Order order: orders){
            OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem: orderItems){
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
                OrderItemDTO orderItemDTO = new OrderItemDTO(orderItem, itemImg.getImgUrl());
                orderHistoryDTO.addOrderItemDTO(orderItemDTO);
            }
            orderHistoryDTOS.add(orderHistoryDTO);
        }
        return new PageImpl<>(orderHistoryDTOS,pageable,totalCount);
    }

    //주문취소(p.323)
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();
        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            //다른 계정은 false
            return false;
        }
        return true;
    }
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    //장바구니상품 주문(p.361)
    public Long orders(List<OrderDTO> orderDTOList, String email){
        //주문할 상품 리스트 만들기
        //현재 로그인 회원정보+상품목록 -> 주문 엔티티
        //주문데이터 저장

        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderDTO orderDTO:orderDTOList){
            log.info("OrderService.orders/itemID : "+orderDTO.getItemId()); //OrderService.orders/itemID : 4 ->cartItemId??
            Item item = itemRepository.findById(orderDTO.getItemId()).orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOderItem(item, orderDTO.getCount());
            orderItemList.add(orderItem);
        }
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);
        return order.getId();
    }

}
