package com.shop.jsshop.controller;

import com.shop.jsshop.dto.MemberFormDTO;
import com.shop.jsshop.entity.Member;
import com.shop.jsshop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor //final, @Notnull 붙은 필드에 생성자 생성
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new") // localhost/members/new
    public String memberForm(Model model){
       model.addAttribute("memberFormDTO", new MemberFormDTO());
        return "member/memberForm";  //resources/templates/member/memberForm.html
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDTO memberFormDTO, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){ //bindingResult: @Valid 검사 결과 담은 객체
            return "member/memberForm";
        }

        try {
            Member member = new Member().createMember(memberFormDTO, passwordEncoder); //ROLE_USER
            memberService.saveMember(member);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해 주세요.");
        return "member/memberLoginForm";
    }

}

//post - login시 >SecurityConfig로 작동
//    Hibernate:
//    select
//    m1_0.member_id,
//    m1_0.address,
//    m1_0.email,
//    m1_0.name,
//    m1_0.password,
//    m1_0.role
//            from
//    member m1_0
//    where
//    m1_0.email=?
//    Hibernate:
//    select
//    m1_0.member_id,
//    m1_0.address,
//    m1_0.email,
//    m1_0.name,
//    m1_0.password,
//    m1_0.role
//            from
//    member m1_0
//    where
//    m1_0.email=?
