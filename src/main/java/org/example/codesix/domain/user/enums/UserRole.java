package org.example.codesix.domain.user.enums;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;


@Getter
public enum UserRole {



    USER("user"),

    ADMIN("admin");


    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public static UserRole of(String roleName) throws IllegalArgumentException {
        for(UserRole role : values()) {
            if(role.getName().equals(roleName.toLowerCase())) {
                return role;
            }
        }
        throw new IllegalArgumentException("해당하는 권한을 찾을 수 없습니다." + roleName);
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.name()));
    }

}
