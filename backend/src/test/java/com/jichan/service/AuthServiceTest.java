package com.jichan.service;

import com.jichan.dto.AuthDto.AuthResponse;
import com.jichan.dto.AuthDto.LoginRequest;
import com.jichan.dto.AuthDto.ResetPasswordRequest;
import com.jichan.dto.AuthDto.SignupRequest;
import com.jichan.entity.User;
import com.jichan.repository.ContactLogRepository;
import com.jichan.repository.RatingRepository;
import com.jichan.repository.UserRepository;
import com.jichan.repository.UserSpecialtyRepository;
import com.jichan.util.Encryptor;
import com.jichan.util.JwtUtil;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository          userRepository;
    @Mock
    private PasswordEncoder         passwordEncoder;
    @Mock
    private JwtUtil                 jwtUtil;
    @Mock
    private EmailService            emailService;
    @Mock
    private Encryptor               encryptor;
    @Mock
    private UserSpecialtyRepository userSpecialtyRepository;
    @Mock
    private RatingRepository        ratingRepository;
    @Mock
    private ContactLogRepository    contactLogRepository;

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() throws MessagingException {
        // given
        SignupRequest request = new SignupRequest("test", "test@example.com", "password123!");
        given(userRepository.findByEmail(request.email())).willReturn(Optional.empty());
        given(passwordEncoder.encode(request.password())).willReturn("encodedPassword");
        given(encryptor.encrypt(anyString())).willReturn("encryptedToken");

        // when
        authService.signup(request);

        // then
        verify(userRepository).save(any(User.class));
        verify(emailService).sendVerificationEmail(anyString(), anyString());
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 인증된 이메일")
    void signup_fail_already_verified() {
        // given
        SignupRequest request = new SignupRequest("test", "test@example.com", "password123!");
        User existingUser = User.builder()
                .email(request.email())
                .emailVerified(true)
                .build();
        given(userRepository.findByEmail(request.email())).willReturn(Optional.of(existingUser));

        // when & then
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용 중인 이메일입니다.");
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "password123!");
        User user = User.builder()
                .id(1L)
                .email(request.email())
                .password("encodedPassword")
                .emailVerified(true)
                .build();

        given(userRepository.findByEmail(request.email())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.password(), user.getPassword())).willReturn(true);
        given(jwtUtil.generateAccessToken(user.getId())).willReturn("accessToken");
        given(jwtUtil.generateRefreshToken(user.getId())).willReturn("refreshToken");

        // when
        AuthResponse response = authService.login(request);

        // then
        assertThat(response.accessToken()).isEqualTo("accessToken");
        assertThat(response.refreshToken()).isEqualTo("refreshToken");
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 미인증")
    void login_fail_email_not_verified() {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "password123!");
        User user = User.builder()
                .email(request.email())
                .password("encodedPassword")
                .emailVerified(false)
                .build();

        given(userRepository.findByEmail(request.email())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.password(), user.getPassword())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일 인증이 완료되지 않았습니다. 이메일을 확인해주세요.");
    }

    @Test
    @DisplayName("토큰 갱신 성공")
    void refreshToken_success() {
        // given
        String refreshToken = "validRefreshToken";
        Long   userId       = 1L;

        given(jwtUtil.validateToken(refreshToken)).willReturn(true);
        given(jwtUtil.getUserIdFromToken(refreshToken)).willReturn(userId);
        given(jwtUtil.generateAccessToken(userId)).willReturn("newAccessToken");

        // when
        String newAccessToken = authService.refreshToken(refreshToken);

        // then
        assertThat(newAccessToken).isEqualTo("newAccessToken");
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void withdrawUser_success() {
        // given
        Long userId = 1L;
        User user   = User.builder()
                .id(userId)
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        authService.withdrawUser(userId);

        // then
        verify(userSpecialtyRepository).deleteAllByUserId(userId);
        verify(ratingRepository).deleteAllByAllUserId(userId);
        verify(contactLogRepository).deleteAllByAllUserId(userId);
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("이메일 인증 성공")
    void verifyEmail_success() {
        // given
        String token          = "encryptedToken";
        Long   userId         = 1L;
        long   timestamp      = System.currentTimeMillis();
        String decryptedToken = "email_verify-" + userId + "-" + timestamp;

        given(encryptor.decrypt(token)).willReturn(decryptedToken);
        User user = User.builder()
                .id(userId)
                .emailVerified(false)
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        authService.verifyEmail(token);

        // then
        assertThat(user.getEmailVerified()).isTrue();
    }

    @Test
    @DisplayName("비밀번호 재설정 요청 성공")
    void forgotPassword_success() throws MessagingException {
        // given
        String email = "test@example.com";
        User   user  = User.builder()
                .id(1L)
                .email(email)
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(encryptor.encrypt(anyString())).willReturn("encryptedToken");

        // when
        authService.forgotPassword(email);

        // then
        verify(emailService).sendPasswordResetEmail(anyString(), anyString());
    }

    @Test
    @DisplayName("비밀번호 재설정 성공")
    void resetPassword_success() {
        // given
        ResetPasswordRequest request        = new ResetPasswordRequest("encryptedToken", "newPassword123!");
        Long                 userId         = 1L;
        long                 timestamp      = System.currentTimeMillis();
        String               decryptedToken = "password_reset-" + userId + "-" + timestamp;

        given(encryptor.decrypt(request.token())).willReturn(decryptedToken);
        User user = User.builder()
                .id(userId)
                .password("oldPassword")
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.encode(request.password())).willReturn("encodedNewPassword");

        // when
        authService.resetPassword(request);

        // then
        assertThat(user.getPassword()).isEqualTo("encodedNewPassword");
    }
}
