package org.example.codesix.global.config;

import java.util.List;

public class SecurityConstants {
    public static final List<String> WHITE_LIST = List.of(
            "/api/users/signup",
            "/api/users/login"
    );
}
