package com.shop.jsshop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    //Auditing 이용한 Entity 공통 속성 공통화(regTime, UpdateTime)
    @Override
    public Optional<String> getCurrentAuditor() {
        //현재 로그인한 사용자 정보를 조회하여 사용자의 이름을 등록자, 수정자로 지정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userID ="";
        if(authentication !=null){
            userID = authentication.getName();
        }
        return Optional.of(userID);
    }


}
