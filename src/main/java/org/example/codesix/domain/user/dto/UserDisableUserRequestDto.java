package org.example.codesix.domain.user.dto;

import lombok.Getter;

@Getter
public class UserDisableUserRequestDto {

    private String password;

    public UserDisableUserRequestDto(String password) {
        this.password = password;
    }
}
