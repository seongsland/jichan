package com.jichan.service;

import com.jichan.dto.UserDto.ProfileResponse;
import com.jichan.dto.UserDto.ProfileUpdateRequest;
import com.jichan.entity.User;
import com.jichan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getGender(),
                user.getRegion(),
                user.getIntroduction(),
                user.getIsVisible(),
                user.getPhone(),
                user.getPhoneMessage()
        );
    }

    public ProfileResponse updateProfile(String email, ProfileUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        user.updateProfile(
                request.name(),
                request.gender(),
                request.region(),
                request.introduction(),
                request.isVisible(),
                request.phone(),
                request.phoneMessage()
        );

        userRepository.save(user);

        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getGender(),
                user.getRegion(),
                user.getIntroduction(),
                user.getIsVisible(),
                user.getPhone(),
                user.getPhoneMessage()
        );
    }
}
