package com.shop.jsshop.dto;

import com.shop.jsshop.constant.OrderStatus;
import com.shop.jsshop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistoryDTO { //주문정보(p.310)
    private Long orderId; //주문아이디
    private String orderDate; //주문날짜
    private OrderStatus orderStatus; //주문상태
    private List<OrderItemDTO> orderItemDTOList = new ArrayList<>(); //주문상품 리스트

    //생성자
    public OrderHistoryDTO(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus=order.getOrderStatus();
    }

    //주문상품 리스트 추가 메서드
    public void addOrderItemDTO(OrderItemDTO orderItemDTO){
        orderItemDTOList.add(orderItemDTO);
    }
}
