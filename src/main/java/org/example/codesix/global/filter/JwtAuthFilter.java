package org.example.codesix.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.codesix.global.util.AuthenticationScheme;
import org.example.codesix.global.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    private static final List<String> WHITE_LIST = List.of("/api/users/login", "/api/users/signup", "/api/error");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 화이트리스트에 포함되지 않으면 인증을 수행
        if (!WHITE_LIST.contains(request.getRequestURI())) {
            this.authenticate(request);
        }
        filterChain.doFilter(request, response); // 필터 체인 계속 진행
    }

    private void authenticate(HttpServletRequest request) {
        String token = this.getTokenFromRequest(request); // 요청 헤더에서 먼저 토큰을 찾음
        if (token == null) {
            token = this.getTokenFromCookie(request); // 없으면 쿠키에서 토큰을 찾음
        }

        if (token != null && jwtUtil.validToken(token)) {  // 토큰이 유효한지 체크
            String username = this.jwtUtil.getUserName(token); // 토큰에서 사용자 이름 추출

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username); // 사용자 정보 로드
            this.setAuthentication(request, userDetails); // 인증 객체 설정
        }
    }

    // 쿠키에서 JWT 토큰을 추출하는 메서드
    private String getTokenFromCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "Authorization".equals(cookie.getName())) // 쿠키 이름이 Authorization인 경우
                .map(cookie -> cookie.getValue()) // JWT 토큰 값 반환
                .findFirst()
                .orElse(null); // 없으면 null 반환
    }

    // Authorization 헤더에서 JWT 토큰을 추출하는 메서드
    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String headerPrefix = AuthenticationScheme.generateType(AuthenticationScheme.BEARER);

        boolean tokenFound = StringUtils.hasText(bearerToken) && bearerToken.startsWith(headerPrefix);
        if (tokenFound) {
            return bearerToken.substring(headerPrefix.length()); // Bearer 접두사 이후의 토큰 반환
        }
        return null; // 토큰이 없으면 null 반환
    }

    // 인증 객체 설정
    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 인증을 SecurityContext에 설정
    }
}
