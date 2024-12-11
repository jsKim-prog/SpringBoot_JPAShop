package com.shop.jsshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //업로드 파일 읽어올 경로 설정
    
    @Value("${uploadPath}")
    String uploadPath;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/images/**") //브라우저의 url 중 /images/로 시작하면 uploadPath 기준으로 파일 읽어오기
                .addResourceLocations(uploadPath); //로컬 컴퓨터에 저장된 파일을 읽어롤 root 경로
    }
}
