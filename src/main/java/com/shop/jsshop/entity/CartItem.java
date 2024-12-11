package com.shop.jsshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@ToString
public class CartItem extends BaseEntity{ //장바구니 상품들

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //다대일
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY) //다대일 : 하나의 상품은 여러 장바구니 아이템으로 담김
    @JoinColumn(name = "item_id")
    private Item item;

    private int count; //장바구니 담을 개수(상품당)

    //메소드(p.331)
    public static CartItem createCartItem(Cart cart, Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    public void addCount(int count){
        this.count += count;
    }

    //수량변경(p.351)
    public void updateCount(int count){
        this.count = count;
    }
}

//Hibernate:
//        create table cart_item (
//        cart_item bigint not null auto_increment,
//        count integer not null,
//        cart_id bigint,
//        item_id bigint,
//        primary key (cart_item)
//        ) engine=InnoDB
//        Hibernate:
//        alter table if exists cart_item
//        add constraint FK1uobyhgl1wvgt1jpccia8xxs3
//        foreign key (cart_id)
//        references cart (cart_id)
//        Hibernate:
//        alter table if exists cart_item
//        add constraint FK7bt304lncrnya10jnrrwmo654
//        foreign key (item_id)
//        references tb_item (item_id)