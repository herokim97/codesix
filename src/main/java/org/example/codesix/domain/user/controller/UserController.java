package org.example.codesix.domain.user.controller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.user.dto.UserLoginRequestDto;
import org.example.codesix.domain.user.dto.UserLoginResponseDto;
import org.example.codesix.domain.user.dto.UserSignupRequestDto;
import org.example.codesix.domain.user.dto.UserSignupResponseDto;
import org.example.codesix.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //회원 가입
    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponseDto> createUser(@Valid @RequestBody UserSignupRequestDto userSignupRequestDto) {
        UserSignupResponseDto userSignupResponseDto =
                userService.signUp(userSignupRequestDto);

        return new ResponseEntity<>(userSignupResponseDto, HttpStatus.CREATED);
    }

    //사용자 로그인
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto,
                                                      HttpServletResponse response) {
        UserLoginResponseDto userLoginResponseDto =
                userService.login(userLoginRequestDto, response);

        return new ResponseEntity<>(userLoginResponseDto, HttpStatus.OK);
    }

//    //GetMapping
//    @GetMapping("/{id}")
//    public ResponseEntity<UserLoginResponseDto> getUser(@PathVariable Long id) {
//        UserLoginResponseDto userLoginResponseDto =
//                userService.getUser(id);
//
//        return new ResponseEntity<>(userLoginResponseDto, HttpStatus.OK);
//    }


}
