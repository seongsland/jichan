package com.jichan.service;

import com.jichan.dto.AuthDto.AuthResponse;
import com.jichan.dto.AuthDto.LoginRequest;
import com.jichan.dto.AuthDto.SignupRequest;
import com.jichan.entity.User;
import com.jichan.repository.UserRepository;
import com.jichan.util.Encryptor;
import com.jichan.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final Encryptor encryptor; // Added this field

    public void signup(SignupRequest request) {

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
        String token = generateEmailVerificationToken(user.getId());
        String encryptedToken = encryptor.encrypt(token); // Encrypt the token
        emailService.sendVerificationEmail(user.getEmail(), encryptedToken);
    }

    public void verifyEmail(String token) {
        String decryptedToken = encryptor.decrypt(token); // Decrypt the token
        Long[] tokenData = parseEmailVerificationToken(decryptedToken);
        Long userId = tokenData[0];
        Long tokenTimestamp = tokenData[1];
        
        // Check if token is expired (30 minutes)
        long currentTime = System.currentTimeMillis();
        if (currentTime - tokenTimestamp > 1800000) {
            throw new IllegalArgumentException("만료된 토큰입니다. 새로운 인증 메일을 요청해주세요.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));
        user.verifyEmail();
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

    private String generateEmailVerificationToken(Long userId) {
        return "email-verify-" + userId + "-" + System.currentTimeMillis();
    }

    private Long[] parseEmailVerificationToken(String token) {
        try {
            String[] parts = token.split("-");
            return new Long[]{Long.parseLong(parts[2]), Long.parseLong(parts[3])};
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }
}
