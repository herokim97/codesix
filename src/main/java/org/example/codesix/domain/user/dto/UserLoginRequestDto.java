package org.example.codesix.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.example.codesix.domain.user.enums.UserRole;

@Getter
public class UserLoginRequestDto {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수값 입니다.")
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*()-+=]).{8,}$",
            message = "비밀번호는 최소 8글자 이상, 대문자, 소문자, 숫자, 특수문자를 각각 최소 1개 포함해야 합니다."
    )
    @NotBlank(message = "패스워드는 필수값 입니다.")
    private String password;


    public UserLoginRequestDto(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
