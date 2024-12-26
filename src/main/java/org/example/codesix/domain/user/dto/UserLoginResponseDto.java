package org.example.codesix.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginResponseDto {

    private Long id;

    private String email;

    private String message;

    private String tokenAuthScheme;

    private String accessToken;

    public UserLoginResponseDto(Long id, String email, String message, String tokenAuthScheme) {
        this.id = id;
        this.email = email;
        this.message = message;
        this.tokenAuthScheme = tokenAuthScheme;
    }

    public UserLoginResponseDto(Long id, String email, String message) {
        this.id = id;
        this.email = email;
        this.message = message;
    }
}
