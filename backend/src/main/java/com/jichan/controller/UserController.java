package com.jichan.controller;

import com.jichan.dto.UserDto.ProfileResponse;
import com.jichan.dto.UserDto.ProfileUpdateRequest;
import com.jichan.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        ProfileResponse response = userService.getProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody ProfileUpdateRequest request) {
        Long userId = Long.valueOf(authentication.getName());
        ProfileResponse response = userService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }
}
