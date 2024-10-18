package com.shop.jsshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberFormDTO { //회원가입->사용자 화면에서 오는 사용자 정보
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name; //성명

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해 주세요.")
    private String email; //이메일

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해 주세요.")
    private String password; //패스워드

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address; //주소
}
