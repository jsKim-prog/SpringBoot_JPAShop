package com.shop.jsshop.entity;

import com.shop.jsshop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class Order extends BaseEntity { //주문 entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //다대일
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태(order, cancel)

    //orderItem 매핑
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    //연관관계의 주인은 OrderItem, -> mappedBy = "order" : OrderItem의 order 에 의해 관리됨
    //CascadeType.ALL : 부모 영속성 상태변화를 자식 엔티티에 모두 전이
    //orphanRemoval = true : 고아객체 제거-> 주문에서 제거 시 orderItem 엔티티 제거
    private List<OrderItem> orderItems = new ArrayList<>();

    //메서드 : 주문생성(p.298)
    //OrderItem 객체에 주문정보 담아주기
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    //주문객체 생성
    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        //멤버정보와 주문아이템 정보 받아 주문객체에 넣음
        Order order = new Order();
        order.setMember(member);
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        //주문일, 상태 세팅
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ORDER);
        return order;
    }

    //총 주문금액
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    //주문취소
    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem: orderItems){
            orderItem.cancel();
        }
    }

}
//Hibernate:
//        create table orders (
//        order_id bigint not null auto_increment,
//        order_date datetime(6),
//        order_status enum ('CANCEL','ORDER'),
//        reg_time datetime(6),
//        update_time datetime(6),
//        member_id bigint,
//        primary key (order_id)
//        ) engine=InnoDB
//        Hibernate:
//        alter table if exists order_item
//        add constraint FKbf1suslw6lutwqdmsbfq90ekx
//        foreign key (item_id)
//        references tb_item (item_id)
//        Hibernate:
//        alter table if exists order_item
//        add constraint FKt4dc2r9nbvbujrljv3e23iibt
//        foreign key (order_id)
//        references orders (order_id)
//        Hibernate:
//        alter table if exists orders
//        add constraint FKpktxwhj3x9m4gth5ff6bkqgeb
//        foreign key (member_id)
//        references member (member_id)