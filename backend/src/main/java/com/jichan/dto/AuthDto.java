package com.jichan.dto;

import com.jichan.util.validator.ValidName;
import com.jichan.util.validator.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDto {

    @Schema(description = "회원가입 요청")
    public record SignupRequest(
            @Schema(description = "사용자 이름", example = "홍길동")
            @NotBlank(message = "이름은 필수입니다") @ValidName String name,
            
            @Schema(description = "이메일 주소", example = "user@example.com")
            @NotBlank(message = "이메일은 필수입니다") @Email(message = "유효한 이메일 형식이 아닙니다") String email,
            
            @Schema(description = "비밀번호 (8~32자)", example = "password123!")
            @NotBlank(message = "비밀번호는 필수입니다") @Size(min = 8, max = 32, message = "비밀번호는 8자 이상 32자 이하이어야 합니다.") @ValidPassword String password) {
    }

    @Schema(description = "로그인 요청")
    public record LoginRequest(
            @Schema(description = "이메일 주소", example = "user@example.com")
            @NotBlank(message = "이메일은 필수입니다") @Email(message = "유효한 이메일 형식이 아닙니다") String email,
            
            @Schema(description = "비밀번호", example = "password123!")
            @NotBlank(message = "비밀번호는 필수입니다") String password) {
    }

    @Schema(description = "인증 응답")
    public record AuthResponse(
            @Schema(description = "액세스 토큰") String accessToken,
            @Schema(description = "리프레시 토큰") String refreshToken) {
    }

    @Schema(description = "비밀번호 찾기 요청")
    public record ForgetPasswordRequest(
            @Schema(description = "이메일 주소", example = "user@example.com")
            @NotBlank(message = "이메일은 필수입니다") @Email(message = "유효한 이메일 형식이 아닙니다") String email) {
    }

    @Schema(description = "비밀번호 재설정 요청")
    public record ResetPasswordRequest(
            @Schema(description = "비밀번호 재설정 토큰")
            @NotBlank(message = "토큰은 필수입니다") String token,
            
            @Schema(description = "새로운 비밀번호 (8~32자)", example = "newPassword123!")
            @NotBlank(message = "비밀번호는 필수입니다") @Size(min = 8, max = 32, message = "비밀번호는 8자 이상 32자 이하이어야 합니다.") @ValidPassword String password) {
    }
}
