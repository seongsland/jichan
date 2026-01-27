package com.jichan.controller;

import com.jichan.dto.AuthDto.AuthResponse;
import com.jichan.dto.AuthDto.LoginRequest;
import com.jichan.dto.AuthDto.ResetPasswordRequest;
import com.jichan.dto.AuthDto.SignupRequest;
import com.jichan.service.AuthService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTestSupport {

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() throws Exception {
        // given
        SignupRequest request = new SignupRequest("test", "test@example.com", "password123!");
        doNothing().when(authService)
                .signup(any(SignupRequest.class));

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다. 이메일을 확인해주세요."));
    }

    @Test
    @DisplayName("이메일 인증 성공")
    void verifyEmail_success() throws Exception {
        // given
        String token = "testToken";
        doNothing().when(authService)
                .verifyEmail(token);

        // when & then
        mockMvc.perform(get("/api/auth/verify_email")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("이메일 인증이 완료되었습니다."));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        // given
        LoginRequest request  = new LoginRequest("test@example.com", "password123!");
        AuthResponse response = new AuthResponse("accessToken", "refreshToken");
        given(authService.login(any(LoginRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("refreshToken"))
                .andExpect(jsonPath("$.accessToken").value("accessToken"));
    }

    @Test
    @DisplayName("토큰 갱신 성공")
    void refreshToken_success() throws Exception {
        // given
        String refreshToken = "validRefreshToken";
        Cookie cookie       = new Cookie("refreshToken", refreshToken);
        given(authService.refreshToken(refreshToken)).willReturn("newAccessToken");

        // when & then
        mockMvc.perform(post("/api/auth/token_refresh")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newAccessToken"));
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() throws Exception {
        // when & then
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("refreshToken", 0))
                .andExpect(jsonPath("$.message").value("로그아웃되었습니다."));
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    @WithMockUser(username = "1")
    void withdrawUser_success() throws Exception {
        // given
        doNothing().when(authService)
                .withdrawUser(1L);

        // when & then
        mockMvc.perform(delete("/api/auth/withdraw"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원탈퇴가 완료되었습니다."));
    }

    @Test
    @DisplayName("비밀번호 재설정 요청 성공")
    void forgotPassword_success() throws Exception {
        // given
        Map<String, String> request = Map.of("email", "test@example.com");
        doNothing().when(authService)
                .forgotPassword(anyString());

        // when & then
        mockMvc.perform(post("/api/auth/forgot_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("비밀번호 재설정 링크를 이메일로 보냈습니다."));
    }

    @Test
    @DisplayName("비밀번호 재설정 성공")
    void resetPassword_success() throws Exception {
        // given
        ResetPasswordRequest request = new ResetPasswordRequest("token", "newPassword123!");
        doNothing().when(authService)
                .resetPassword(any(ResetPasswordRequest.class));

        // when & then
        mockMvc.perform(post("/api/auth/reset_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("비밀번호가 성공적으로 재설정되었습니다."));
    }
}
