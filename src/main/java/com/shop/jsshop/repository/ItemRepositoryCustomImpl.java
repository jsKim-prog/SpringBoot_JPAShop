package com.shop.jsshop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.jsshop.constant.ItemSellStatus;
import com.shop.jsshop.dto.ItemSearchDTO;
import com.shop.jsshop.dto.MainItemDTO;
import com.shop.jsshop.dto.QMainItemDTO;
import com.shop.jsshop.entity.Item;
import com.shop.jsshop.entity.QItem;
import com.shop.jsshop.entity.QItemImg;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    private JPAQueryFactory queryFactory;

    //생성자 : 동적쿼리 생성을 위한 JPAQueryFactory 클래스 활용
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    //메서드
    //상품판매조건에 따른 조회 조건
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null? null: QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    //상품등록 날짜에 대한 조회 조건
    private BooleanExpression regDateAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now(); //현재날짜
        if(StringUtils.equals("all", searchDateType) || searchDateType==null){
            return null;
        }else if (StringUtils.equals("1d",searchDateType)){
            dateTime=dateTime.minusDays(1);
        }else if (StringUtils.equals("1w",searchDateType)){
            dateTime=dateTime.minusWeeks(1);
        }else if (StringUtils.equals("1m",searchDateType)){
            dateTime=dateTime.minusMonths(1);
        }else if (StringUtils.equals("6m",searchDateType)){
            dateTime=dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }

    //검색 키워드에 관한 조회 조건(상품명, 등록자)-관리자용
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemName", searchBy)){
            return QItem.item.itemName.like("%"+searchQuery+"%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }

    //상품명에 관한 조회 조건-사용자용
    private BooleanExpression itemNameLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery)? null:QItem.item.itemName.like("%"+searchQuery+"%");
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {
        //p.269 : QueryResults<Item> -> List<Item> , fetchResult() -> fetch()
        List<Item> content = queryFactory
                .selectFrom(QItem.item).where(regDateAfter(itemSearchDTO.getSearchDateType()), searchSellStatusEq(itemSearchDTO.getSearchSellStatus()), searchByLike(itemSearchDTO.getSearchBy(), itemSearchDTO.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset()) //데이터 가져올 시작 인덱스 지정
                .limit(pageable.getPageSize()) //한번에 가져올 최대 개수
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDateAfter(itemSearchDTO.getSearchDateType()),searchSellStatusEq(itemSearchDTO.getSearchSellStatus()),searchByLike(itemSearchDTO.getSearchBy(), itemSearchDTO.getSearchQuery())).fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override //사용자용 상품리스트(p.282)
    public Page<MainItemDTO> getMainItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        
        List<MainItemDTO> content = queryFactory
                .select(new QMainItemDTO(item.id, item.itemName, item.itemDetail, itemImg.imgUrl, item.price))
                .from(itemImg)
                .join(itemImg.item, item) //itemImg와 item 내부조인
                .where(itemImg.repimgYn.eq("Y")) //대표이미지만 불러오기
                .where(itemNameLike(itemSearchDTO.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory.select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNameLike(itemSearchDTO.getSearchQuery())).fetchOne();
        return new PageImpl<>(content, pageable, total);
    }
}
