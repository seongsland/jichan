package com.jichan.service;

import com.jichan.dto.UserDto.ProfileResponse;
import com.jichan.dto.UserDto.ProfileUpdateRequest;
import com.jichan.dto.UserDto.SpecialtyRequest;
import com.jichan.dto.UserDto.SpecialtyResponse;
import com.jichan.entity.User;
import com.jichan.entity.UserSpecialty;
import com.jichan.repository.UserRepository;
import com.jichan.repository.UserSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserSpecialtyRepository userSpecialtyRepository;
    private final SpecialtyService specialtyService;

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<SpecialtyResponse> specialties = userSpecialtyRepository.findByUserId(userId).stream()
                .map(us -> {
                    var detail = specialtyService.getDetail(us.getSpecialtyDetailId());
                    var category = specialtyService.getCategory(detail.categoryId());
                    return new SpecialtyResponse(
                            detail.id(),
                            detail.name(),
                            category.name(),
                            us.getHourlyRate()
                    );
                })
                .toList();

        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getGender(),
                user.getRegion(),
                user.getIntroduction(),
                user.getIsVisible(),
                user.getPhone(),
                user.getPhoneMessage(),
                specialties
        );
    }

    public ProfileResponse updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
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

        // Delete existing specialties
        userSpecialtyRepository.deleteAllByUserId(userId);

        // Save new specialties
        if (request.specialties() != null) {
            for (SpecialtyRequest specialtyRequest : request.specialties()) {
                // Validate that the specialty detail exists
                UserSpecialty userSpecialty = UserSpecialty.builder()
                        .userId(userId)
                        .specialtyDetailId(specialtyRequest.specialtyDetailId())
                        .hourlyRate(specialtyRequest.hourlyRate())
                        .build();
                userSpecialtyRepository.save(userSpecialty);
            }
        }

        List<SpecialtyResponse> specialties = userSpecialtyRepository.findByUserId(userId).stream()
                .map(us -> {
                    var detail = specialtyService.getDetail(us.getSpecialtyDetailId());
                    var category = specialtyService.getCategory(detail.categoryId());
                    return new SpecialtyResponse(
                            detail.id(),
                            detail.name(),
                            category.name(),
                            us.getHourlyRate()
                    );
                })
                .toList();

        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getGender(),
                user.getRegion(),
                user.getIntroduction(),
                user.getIsVisible(),
                user.getPhone(),
                user.getPhoneMessage(),
                specialties
        );
    }
}
