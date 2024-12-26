package org.example.codesix.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.example.codesix.domain.user.enums.UserRole;

@Getter
public class UserSignupRequestDto {

    @Email(message = "Invalid email format")
    @NotBlank
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*()-+=]).{8,}$", message = "Password must be minimum 8 characters, including letters and numbers")
    @NotBlank
    private String password;

    private final UserRole role;


    public UserSignupRequestDto(String email, String password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
