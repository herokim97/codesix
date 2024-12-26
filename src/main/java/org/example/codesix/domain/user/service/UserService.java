package org.example.codesix.domain.user.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codesix.domain.user.dto.UserLoginRequestDto;
import org.example.codesix.domain.user.dto.UserLoginResponseDto;
import org.example.codesix.domain.user.dto.UserSignupRequestDto;
import org.example.codesix.domain.user.dto.UserSignupResponseDto;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.domain.user.enums.UserRole;
import org.example.codesix.domain.user.enums.UserStatus;
import org.example.codesix.domain.user.repository.UserRepository;
import org.example.codesix.global.exception.CustomException;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.util.AuthenticationScheme;
import org.example.codesix.global.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @Transactional
    public UserSignupResponseDto signUp(UserSignupRequestDto userSignupRequestDto) {
        String email = userSignupRequestDto.getEmail();
        String password = passwordEncoder.encode(userSignupRequestDto.getPassword());
        UserRole role = userSignupRequestDto.getRole();

        if(userRepository.findUserByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "동일한 이메일이 존재합니다.");
        }
        Optional<User> existUser = userRepository.findUserByEmail(email);

        if(existUser.isPresent()) {
            User user = existUser.get();
            if(user.getStatus() == UserStatus.DISABLED) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 삭제된 이메일 입니다.");
            }
        }
        User user = new User(email, password, role);
        User savedUser = userRepository.save(user);


        return new UserSignupResponseDto(savedUser.getId(), savedUser.getEmail(), savedUser.getRole());

    }

    //Login
    @Transactional
    public UserLoginResponseDto login(@Valid UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        String email = userLoginRequestDto.getEmail();
        String password = userLoginRequestDto.getPassword();

        Optional<User> loginUser = userRepository.findUserByEmail(email);

        if(!passwordEncoder.matches(password, loginUser.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "password 일치하지 않음.");
        }

        Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequestDto.getEmail(),
                        userLoginRequestDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = this.jwtUtil.generateToken(auth);
        log.info("토큰 생성 : {}", accessToken);

        ResponseCookie cookie = ResponseCookie.from("Authorization", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(3600)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());


        return new UserLoginResponseDto(loginUser.get().getId(), loginUser.get().getEmail(), "login Success" , AuthenticationScheme.BEARER.getName());
    }

//    //테스트용 유저 조회
//    @Transactional
//    public UserLoginResponseDto getUser(Long id) {
//        Optional<User> user = userRepository.findById(id);
//
//        return new UserLoginResponseDto(user.get().getId(), user.get().getEmail(), "get Success");
//
//
//    }

}
