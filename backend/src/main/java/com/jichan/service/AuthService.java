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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final Encryptor encryptor;
    private final UserSpecialtyRepository userSpecialtyRepository;
    private final RatingRepository ratingRepository;
    private final ContactLogRepository contactLogRepository;

    public void signup(SignupRequest request) throws MessagingException {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            if (user.getEmailVerified()) {
                throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
            } else {
                userRepository.delete(user);
                userRepository.flush();
            }
        });

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .isVisible(false)
                .build();

        userRepository.save(user);

        // 이메일 인증 토큰 생성 및 전송
        String token = generateEmailToken(user.getId(), "email_verify");
        String encryptedToken = encryptor.encrypt(token);
        emailService.sendVerificationEmail(user.getEmail(), encryptedToken);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        if (!user.getEmailVerified()) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다. 이메일을 확인해주세요.");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        return new AuthResponse(accessToken, refreshToken);
    }

    public String refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        return jwtUtil.generateAccessToken(userId);
    }

    public void withdrawUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 1. Delete UserSpecialty entries
        userSpecialtyRepository.deleteAllByUserId(userId);

        // 2. Delete Rating entries (both as rater and as target)
        ratingRepository.deleteAllByAllUserId(userId);

        // 3. Delete ContactLog entries (both as requester and as target)
        contactLogRepository.deleteAllByAllUserId(userId);

        // 4. Delete the User
        userRepository.delete(user);
    }

    public void verifyEmail(String token) {
        String decryptedToken = encryptor.decrypt(token);
        Object[] tokenData = parseEmailToken(decryptedToken, "email_verify");
        Long userId = (Long) tokenData[0];
        Long tokenTimestamp = (Long) tokenData[1];

        // Check if token is expired (30 minutes)
        long currentTime = System.currentTimeMillis();
        if (currentTime - tokenTimestamp > 1800000) {
            throw new IllegalArgumentException("만료된 토큰입니다. 새로운 인증 메일을 요청해주세요.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));
        user.verifyEmail();
    }

    public void forgotPassword(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        String token = generateEmailToken(user.getId(), "password_reset");
        String encryptedToken = encryptor.encrypt(token);
        emailService.sendPasswordResetEmail(user.getEmail(), encryptedToken);
    }

    public void resetPassword(ResetPasswordRequest request) {
        String decryptedToken = encryptor.decrypt(request.token());
        Object[] tokenData = parseEmailToken(decryptedToken, "password_reset");
        Long userId = (Long) tokenData[0];
        Long tokenTimestamp = (Long) tokenData[1];

        // 30분 만료
        long currentTime = System.currentTimeMillis();
        if (currentTime - tokenTimestamp > 1800000) {
            throw new IllegalArgumentException("만료된 토큰입니다. 다시 요청해주세요.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        user.changePassword(passwordEncoder.encode(request.password()));
    }

    private String generateEmailToken(Long userId, String type) {
        return type + "-" + userId + "-" + System.currentTimeMillis();
    }

    private Object[] parseEmailToken(String token, String expectedType) {
        try {
            String[] parts = token.split("-");
            if (!expectedType.equals(parts[0])) {
                throw new IllegalArgumentException("유효하지 않은 토큰 유형입니다.");
            }
            return new Object[]{Long.parseLong(parts[1]), Long.parseLong(parts[2])};
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }
}
