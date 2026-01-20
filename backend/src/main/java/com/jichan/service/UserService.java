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
import org.springframework.util.StringUtils;

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

        if (Boolean.TRUE.equals(request.isVisible())) {
            if (!StringUtils.hasText(request.name()) ||
                !StringUtils.hasText(request.gender()) ||
                !StringUtils.hasText(request.region()) ||
                !StringUtils.hasText(request.introduction())) {
                throw new IllegalArgumentException("프로필을 공개하려면 이름, 성별, 지역, 소개글을 모두 입력해야 합니다.");
            }

            if (StringUtils.hasText(request.phone()) && !StringUtils.hasText(request.phoneMessage())) {
                throw new IllegalArgumentException("핸드폰 번호를 입력한 경우 연락 가능 시간 메모를 입력해야 합니다.");
            }

            if (request.specialties() == null || request.specialties().isEmpty()) {
                throw new IllegalArgumentException("프로필을 공개하려면 최소 1개 이상의 특기를 등록해야 합니다.");
            }
        }

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
