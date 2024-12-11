package com.shop.jsshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class}) //Auditing 적용
@MappedSuperclass
@Getter
@Setter
public abstract class BaseTimeEntity {
    //감시클래스 : 최상위-등록시간, 수정시간 관리
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime; //등록시간

    @LastModifiedDate
    private LocalDateTime updateTime; //수정시간
}
