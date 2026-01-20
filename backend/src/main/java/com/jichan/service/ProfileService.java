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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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
        
        if (sortBy == null || sortBy.isEmpty()) {
            pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("name").ascending());
        }

        Slice<User> userSlice;

        if (specialtyId != null) {
            if ("rating".equals(sortBy)) {
                userSlice = userRepository.findBySpecialtyIdAndIsVisibleTrueOrderByRatingDesc(specialtyId, pageable);
            } else if ("price".equals(sortBy)) {
                userSlice = userRepository.findBySpecialtyIdAndIsVisibleTrueOrderByPriceAsc(specialtyId, pageable);
            } else {
                userSlice = userRepository.findBySpecialtyIdAndIsVisibleTrue(specialtyId, pageable);
            }
        } else {
            if ("rating".equals(sortBy)) {
                userSlice = userRepository.findByIsVisibleTrueOrderByRatingDesc(pageable);
            } else if ("price".equals(sortBy)) {
                userSlice = userRepository.findByIsVisibleTrueOrderByPriceAsc(pageable);
            } else {
                userSlice = userRepository.findByIsVisibleTrue(pageable);
            }
        }

        List<User> users = userSlice.getContent();
        boolean hasNext = userSlice.hasNext();

        // ID 목록 추출
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());

        // 한 번의 쿼리로 필요한 데이터 조회
        Map<Long, List<UserSpecialty>> userSpecialtyMap = userSpecialtyRepository.findByUserIdIn(userIds).stream()
                .collect(Collectors.groupingBy(UserSpecialty::getUserId));

        Map<Long, List<Rating>> ratingMap = ratingRepository.findByExpertIdIn(userIds).stream()
                .collect(Collectors.groupingBy(Rating::getExpertId));

        List<ProfileItem> content = users.stream()
                .map(user -> convertToProfileItem(user, userSpecialtyMap.getOrDefault(user.getId(), List.of()), ratingMap.getOrDefault(user.getId(), List.of())))
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


    private ProfileItem convertToProfileItem(User user, List<UserSpecialty> userSpecialties, List<Rating> ratings) {
        List<SpecialtyInfo> specialties = userSpecialties.stream()
                .map(us -> {
                    var detail = specialtyService.getDetail(us.getSpecialtyDetailId());
                    return new SpecialtyInfo(detail.name(), us.getHourlyRate());
                })
                .collect(Collectors.toList());

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
