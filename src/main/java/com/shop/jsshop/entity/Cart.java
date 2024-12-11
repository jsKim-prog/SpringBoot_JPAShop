package com.shop.jsshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity{ //장바구니
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id; //장바구니 id

    @OneToOne(fetch = FetchType.LAZY) //연관관계(1:1) - entity 간 연관관계 설정 없으면 에러 뜸
    @JoinColumn(name = "member_id") //매핑할 외래키
    private Member member; //매칭되는 회원

    //장바구니 엔티티 생성 메서드(p.331)
    public static Cart createCart(Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}
