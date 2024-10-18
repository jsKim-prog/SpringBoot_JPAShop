package com.shop.jsshop.repository;

import com.shop.jsshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> { //Item 관리용 CRUD
    //상속 2가지 : JpaRepository, Querydsl  QuerydslPredicateExecutor<ItemEnt>

    //C , U : save()
    //R : findByID(), findAll()
    //Query Method 사용
    List<Item> findByItemName(String itemName); //상품명 조회
    List<Item> findByItemNameOrItemDetail(String itemName, String itemDetail); //상품명 or 내용 조회
    List<Item> findByPriceLessThan(Integer price); //조건보다 작은 금액 조회
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price); //정렬조건 추가(내림차순)
    
    //@Query 사용
    @Query("select it from Item it where it.itemDetail like %:itemDetail% order by it.price")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail); //상세설명 검색(유사어)+가격 오름차순
    
    
    //D: delete(), deleteAll()

    //paging 추가
    @Query(value = "select it from Item it where it.id>:id",countQuery = "select count(it) from Item it where it.id>:id")
    Page<Item> getListWithPaging(Long id, Pageable pageable);
}
