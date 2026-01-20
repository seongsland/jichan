package com.jichan.service;

import com.jichan.dto.ProfileDto.ContactViewResponse;
import com.jichan.dto.ProfileDto.ProfileItem;
import com.jichan.dto.ProfileDto.ProfileListResponse;
import com.jichan.dto.ProfileDto.SpecialtyInfo;
import com.jichan.entity.*;
import com.jichan.repository.ContactLogRepository;
import com.jichan.repository.RatingRepository;
import com.jichan.repository.UserRepository;
import com.jichan.repository.UserSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final UserRepository userRepository;
    private final UserSpecialtyRepository userSpecialtyRepository;
    private final SpecialtyService specialtyService;
    private final ContactLogRepository contactLogRepository;
    private final RatingRepository ratingRepository;
    private static final int PAGE_SIZE = 10;


    public ProfileListResponse getProfiles(Long specialtyId, String sortBy, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        List<User> users;
        boolean hasNext;

        if (specialtyId != null) {
            Slice<User> userSlice = userRepository.findBySpecialtyIdAndIsVisibleTrue(specialtyId, pageable);
            users = new ArrayList<>(userSlice.getContent());
            hasNext = userSlice.hasNext();
        } else {
            Slice<User> userSlice = userRepository.findByIsVisibleTrue(pageable);
            users = new ArrayList<>(userSlice.getContent());
            hasNext = userSlice.hasNext();
        }

        if ("rating".equals(sortBy)) {
            users.sort((u1, u2) -> {
                double avgRating1 = ratingRepository.findByExpertId(u1.getId()).stream().mapToInt(Rating::getScore).average().orElse(0.0);
                double avgRating2 = ratingRepository.findByExpertId(u2.getId()).stream().mapToInt(Rating::getScore).average().orElse(0.0);
                return Double.compare(avgRating2, avgRating1);
            });
        } else if ("price".equals(sortBy)) {
            users.sort((u1, u2) -> {
                int minPrice1 = userSpecialtyRepository.findByUserId(u1.getId()).stream().mapToInt(UserSpecialty::getHourlyRate).min().orElse(Integer.MAX_VALUE);
                int minPrice2 = userSpecialtyRepository.findByUserId(u2.getId()).stream().mapToInt(UserSpecialty::getHourlyRate).min().orElse(Integer.MAX_VALUE);
                return Integer.compare(minPrice1, minPrice2);
            });
        } else {
            users.sort(java.util.Comparator.comparing(User::getName));
        }

        List<ProfileItem> content = users.stream()
                .map(this::convertToProfileItem)
                .collect(Collectors.toList());

        return new ProfileListResponse(content, hasNext);
    }

    @Transactional
    public ContactViewResponse viewContact(Long viewerId, Long expertId, ContactType contactType) {
        User expert = userRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("전문가를 찾을 수 없습니다."));

        // ContactLog 기록 (중복 방지)
        contactLogRepository.findByViewerIdAndExpertIdAndContactType(viewerId, expertId, contactType)
                .orElseGet(() -> {
                    ContactLog contactLog = ContactLog.builder()
                            .viewerId(viewerId)
                            .expertId(expertId)
                            .contactType(contactType)
                            .build();
                    return contactLogRepository.save(contactLog);
                });

        // 연락처 반환
        if (contactType == ContactType.EMAIL) {
            return new ContactViewResponse(expert.getEmail(), null);
        } else {
            return new ContactViewResponse(expert.getPhone(), expert.getPhoneMessage());
        }
    }


    private ProfileItem convertToProfileItem(User user) {
        // UserSpecialty 조회
        List<UserSpecialty> userSpecialties = userSpecialtyRepository.findByUserId(user.getId());
        List<SpecialtyInfo> specialties = userSpecialties.stream()
                .map(us -> {
                    var detail = specialtyService.getDetail(us.getSpecialtyDetailId());
                    return new SpecialtyInfo(detail.name(), us.getHourlyRate());
                })
                .collect(Collectors.toList());

        // 평균 평점 계산
        List<Rating> ratings = ratingRepository.findByExpertId(user.getId());
        Double averageRating = ratings.isEmpty() ? null :
                ratings.stream()
                        .mapToInt(Rating::getScore)
                        .average()
                        .orElse(0.0);

        return new ProfileItem(
                user.getId(),
                user.getName(),
                user.getGender(),
                user.getRegion(),
                specialties,
                user.getIntroduction(),
                averageRating
        );
    }
}
