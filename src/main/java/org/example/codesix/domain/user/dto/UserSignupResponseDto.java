package org.example.codesix.domain.user.dto;

import lombok.Getter;
import org.example.codesix.domain.user.enums.UserRole;

@Getter
public class UserSignupResponseDto {
    private Long id;

    private String email;

    private UserRole role;

    public UserSignupResponseDto(Long id, String email, UserRole role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
