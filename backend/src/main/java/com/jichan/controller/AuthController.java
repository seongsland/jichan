package com.jichan.controller;

import com.jichan.dto.AuthDto.*;
import com.jichan.dto.CommonDto.ApiResponse;
import com.jichan.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    public ResponseEntity<ApiResponse<Void>> signup(
            @Valid @RequestBody SignupRequest request) throws MessagingException {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("회원가입이 완료되었습니다. 이메일을 확인해주세요.", null));
    }

    @GetMapping("/verify_email")
    @Operation(summary = "이메일 인증", description = "이메일 인증 토큰을 검증합니다.")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok(new ApiResponse<>("이메일 인증이 완료되었습니다.", null));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인을 처리하고 액세스 토큰을 발급합니다.")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);

        // refreshToken을 httpOnly 쿠키로 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", response.refreshToken()).httpOnly(true)
                                                          .secure(true).path("/")
                                                          .maxAge(refreshTokenExpiration / 1000) // 초 단위로 변환
                                                          .sameSite("Strict") // SameSite 설정
                                                          .build();

        // 응답에서 refreshToken 제거 (쿠키로만 전송)
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                             .body(new AuthResponse(response.accessToken(), null));
    }

    @PostMapping("/token_refresh")
    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = null;

        // 쿠키에서 refreshToken 읽기
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            log.info("refreshToken 쿠키가 없습니다.");
        }

        String newAccessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new AuthResponse(newAccessToken, null));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자를 로그아웃 처리하고 리프레시 토큰 쿠키를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // refreshToken 쿠키 삭제
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "").httpOnly(true).secure(true)
                                                          .path("/").maxAge(0) // 즉시 만료
                                                          .sameSite("Strict").build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                             .body(new ApiResponse<>("로그아웃되었습니다.", null));
    }

    @DeleteMapping("/withdraw")
    @Operation(summary = "회원 탈퇴", description = "사용자 계정을 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> withdrawUser(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        authService.withdrawUser(userId);

        return ResponseEntity.ok().body(new ApiResponse<>("회원탈퇴가 완료되었습니다.", null));
    }

    @PostMapping("/forgot_password")
    @Operation(summary = "비밀번호 찾기", description = "비밀번호 재설정 링크를 이메일로 전송합니다.")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgetPasswordRequest request) throws MessagingException {
        authService.forgotPassword(request.email());
        return ResponseEntity.ok(new ApiResponse<>("비밀번호 재설정 링크를 이메일로 보냈습니다.", null));
    }

    @PostMapping("/reset_password")
    @Operation(summary = "비밀번호 재설정", description = "새로운 비밀번호로 변경합니다.")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(new ApiResponse<>("비밀번호가 성공적으로 재설정되었습니다.", null));
    }
}
