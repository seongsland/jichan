package com.jichan.controller;

import com.jichan.dto.UserDto.ProfileResponse;
import com.jichan.dto.UserDto.ProfileUpdateRequest;
import com.jichan.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "프로필 조회", description = "로그인한 사용자의 프로필 정보를 조회합니다.")
    public ResponseEntity<ProfileResponse> getProfile(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        ProfileResponse response = userService.getProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    @Operation(summary = "프로필 수정", description = "로그인한 사용자의 프로필 정보를 수정합니다.")
    public ResponseEntity<ProfileResponse> updateProfile(Authentication authentication,
                                                         @Valid @RequestBody ProfileUpdateRequest request) {
        Long userId = Long.valueOf(authentication.getName());
        ProfileResponse response = userService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }
}
