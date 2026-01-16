package com.jichan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jichan.dto.AuthDto.LoginRequest;
import com.jichan.dto.AuthDto.SignupRequest;
import com.jichan.dto.AuthDto.TokenRefreshRequest;
import com.jichan.entity.User;
import com.jichan.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() throws Exception {
        // given
        SignupRequest request = new SignupRequest("홍길동", "test@example.com", "password123");

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signup_fail_duplicateEmail() throws Exception {
        // given
        User existingUser = User.builder()
                .name("기존사용자")
                .email("test@example.com")
                .password(passwordEncoder.encode("password123"))
                .isVisible(false)
                .build();
        userRepository.save(existingUser);

        SignupRequest request = new SignupRequest("홍길동", "test@example.com", "password123");

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패 - 필수 필드 누락")
    void signup_fail_missingFields() throws Exception {
        // given
        SignupRequest request = new SignupRequest("홍길동", "", "password123");

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 인증 성공")
    void verifyEmail_success() throws Exception {
        // given
        String token = "valid-token";

        // when & then
        mockMvc.perform(post("/api/auth/verify_email")
                        .param("token", token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일 인증 실패 - 유효하지 않은 토큰")
    void verifyEmail_fail_invalidToken() throws Exception {
        // given
        String token = "invalid-token";

        // when & then
        mockMvc.perform(post("/api/auth/verify_email")
                        .param("token", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        // given
        User user = User.builder()
                .name("홍길동")
                .email("test@example.com")
                .password(passwordEncoder.encode("password123"))
                .isVisible(false)
                .build();
        userRepository.save(user);

        LoginRequest request = new LoginRequest("test@example.com", "password123");

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 이메일")
    void login_fail_invalidEmail() throws Exception {
        // given
        LoginRequest request = new LoginRequest("wrong@example.com", "password123");

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_fail_invalidPassword() throws Exception {
        // given
        User user = User.builder()
                .name("홍길동")
                .email("test@example.com")
                .password(passwordEncoder.encode("password123"))
                .isVisible(false)
                .build();
        userRepository.save(user);

        LoginRequest request = new LoginRequest("test@example.com", "wrongpassword");

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("토큰 갱신 성공")
    void refreshToken_success() throws Exception {
        // given
        TokenRefreshRequest request = new TokenRefreshRequest("valid-refresh-token");

        // when & then
        mockMvc.perform(post("/api/auth/token_refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 유효하지 않은 refreshToken")
    void refreshToken_fail_invalidToken() throws Exception {
        // given
        TokenRefreshRequest request = new TokenRefreshRequest("invalid-refresh-token");

        // when & then
        mockMvc.perform(post("/api/auth/token_refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() throws Exception {
        // when & then
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk());
    }
}
