package com.jichan.dto;

import com.jichan.util.validator.ValidName;
import com.jichan.util.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDto {

    public record SignupRequest(
            @NotBlank(message = "이름은 필수입니다")
            @ValidName
            String name,
            @NotBlank(message = "이메일은 필수입니다") @Email(message = "유효한 이메일 형식이 아닙니다") String email,
            @NotBlank(message = "비밀번호는 필수입니다")
            @Size(min = 8, max = 32, message = "비밀번호는 8자 이상 32자 이하이어야 합니다.")
            @ValidPassword
            String password
    ) {}

    public record LoginRequest(
            @NotBlank(message = "이메일은 필수입니다") @Email(message = "유효한 이메일 형식이 아닙니다") String email,
            @NotBlank(message = "비밀번호는 필수입니다") String password
    ) {}

    public record TokenRefreshRequest(
            @NotBlank(message = "refreshToken은 필수입니다") String refreshToken
    ) {}

    public record AuthResponse(
            String accessToken,
            String refreshToken
    ) {}

    public record ResetPasswordRequest(
            @NotBlank(message = "토큰은 필수입니다") String token,
            @NotBlank(message = "비밀번호는 필수입니다")
            @Size(min = 8, max = 32, message = "비밀번호는 8자 이상 32자 이하이어야 합니다.")
            @ValidPassword
            String password
    ) {}
}
