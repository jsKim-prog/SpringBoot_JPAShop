package com.shop.jsshop.repository;

import com.shop.jsshop.constant.ItemSellStatus;
import com.shop.jsshop.entity.Item;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
public class ItemRepositoryTests {
    @Autowired
    ItemRepository itemRepository;

    @Test //삽입(더미데이터)
    public void insertDummyTest(){
        IntStream.rangeClosed(1, 50).forEach(
                i-> {
                    Item item = new Item();
                    item.setItemName("newProduct"+i);
                    item.setPrice(10000+i);
                    item.setItemDetail("상품설명"+i);
                    item.setItemSellStatus(ItemSellStatus.SELL);
                    item.setStockNumber(100);
                    item.setRegTime(LocalDateTime.now());
                    item.setUpdateTime(LocalDateTime.now());

                    itemRepository.save(item);
                }
        );

    }

    @Test //테이블 초기화
    public void truncateTable(){
        itemRepository.deleteAll();
    }


    //상품명-> 리스트 조회
    @Test
    @DisplayName("상품명 조회 테스트")
    public void searchNameTest(){
        List<Item> items = itemRepository.findByItemName("newProduct5");
        for (Item item: items){
            System.out.println(item);
        }
        //ItemVO(id=5, itemName=newProduct5, price=10000, stockNumber=100, itemDetail=상품설명5, itemSellStatus=SELL, regTime=2024-10-10T17:35:29.156002, updateTime=2024-10-10T17:35:29.156002)
    }

    @Test
    public void searchNameOrDetail(){
        List<Item> results= itemRepository.findByItemNameOrItemDetail("newProduct3", "상품설명9");
        for (Item item:results){
            System.out.println(item);
        }
//        ItemVO(id=3, itemName=newProduct3, price=10000, stockNumber=100, itemDetail=상품설명3, itemSellStatus=SELL, regTime=2024-10-10T17:35:29.143939, updateTime=2024-10-10T17:35:29.143939)
//        ItemVO(id=9, itemName=newProduct9, price=10000, stockNumber=100, itemDetail=상품설명9, itemSellStatus=SELL, regTime=2024-10-10T17:35:29.181235, updateTime=2024-10-10T17:35:29.181235)

    }

    @Test
    public void priceLessThanTest(){
        List<Item> items = itemRepository.findByPriceLessThan(10008);
        items.forEach(item -> System.out.println(item));
    }

    @Test
    @DisplayName("상품설명 조회 테스트")
    public void findDetailLikeTests(){
        List<Item> items = itemRepository.findByItemDetail("상품설명");
        items.forEach(item-> System.out.println(item)); //like 적용, 모든 상품 조회
    }



}
