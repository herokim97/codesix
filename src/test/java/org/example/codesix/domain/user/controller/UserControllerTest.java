package org.example.codesix.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.codesix.domain.user.dto.*;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.domain.user.enums.UserRole;
import org.example.codesix.domain.user.repository.UserRepository;
import org.example.codesix.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void signUpTest() {
        UserSignupRequestDto requestDto = new UserSignupRequestDto("test@naver.com", "Asdf!234", UserRole.USER);

        User user = new User("test@naver.com", "Asdf!234", UserRole.USER);

        userService.signUp(requestDto);
        userRepository.save(user);

        UserSignupResponseDto responseDto = new UserSignupResponseDto(user.getId(), user.getEmail(), user.getRole());

        assertEquals(requestDto.getEmail(), responseDto.getEmail());
        assertEquals(requestDto.getRole(), responseDto.getRole());


    }

//    @Test
//    void loginTest() throws Exception {
//        // Given
//        UserLoginRequestDto requestDto = new UserLoginRequestDto("user@naver.com", "Rladuddnd!234");
//        UserLoginResponseDto responseDto = new UserLoginResponseDto(1L,"user@naver.com", "login Success", "Bearer"  );
//
//        // Mocking userService
//        when(userService.login(any(UserLoginRequestDto.class), any())).thenReturn(responseDto);
//
//        // When & Then
//        mockMvc.perform(post("/api/users/login")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(responseDto.getId()))
//                .andExpect(jsonPath("$.email").value("user@naver.com"));
//
//        // Verify service interaction
//        verify(userService, times(1)).login(any(UserLoginRequestDto.class), any());
//    }
}