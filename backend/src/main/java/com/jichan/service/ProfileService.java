package com.jichan.service;

import com.jichan.dto.ProfileDto;
import com.jichan.dto.ProfileDto.ContactViewResponse;
import com.jichan.dto.ProfileDto.ProfileItem;
import com.jichan.dto.ProfileDto.ProfileListResponse;
import com.jichan.dto.ProfileDto.SpecialtyInfo;
import com.jichan.entity.ContactLog;
import com.jichan.entity.ContactType;
import com.jichan.entity.User;
import com.jichan.entity.UserSpecialty;
import com.jichan.repository.ContactLogRepository;
import com.jichan.repository.UserRepository;
import com.jichan.repository.UserSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private static final int PAGE_SIZE = 10;
    private final UserRepository userRepository;
    private final UserSpecialtyRepository userSpecialtyRepository;
    private final SpecialtyService specialtyService;
    private final ContactLogRepository contactLogRepository;

    public ProfileListResponse getProfiles(Long viewerId, ProfileDto.ProfileRequest profileRequest) {
        Pageable pageable = PageRequest.of(profileRequest.page(), PAGE_SIZE);

        // QueryDSL을 사용하여 조건에 맞는 프로필 조회
        Slice<User> userSlice = userRepository.findProfiles(profileRequest, pageable);

        List<User> users = userSlice.getContent();
        boolean hasNext = userSlice.hasNext();

        // ID 목록 추출
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());

        // 한 번의 쿼리로 필요한 데이터 조회
        Map<Long, List<UserSpecialty>> userSpecialtyMap = userSpecialtyRepository.findByUserIdIn(userIds).stream()
                                                                                 .collect(Collectors.groupingBy(
                                                                                         UserSpecialty::getUserId));

        // ContactLog 조회 (현재 사용자가 본 기록)
        Set<Long> emailViewedExperts = Collections.emptySet();
        Set<Long> phoneViewedExperts = Collections.emptySet();

        if (viewerId != null) {
            List<ContactLog> viewerContactLogs = contactLogRepository.findByViewerIdAndExpertIdIn(viewerId, userIds);
            emailViewedExperts = viewerContactLogs.stream()
                                                  .filter(log -> log.getContactType() == ContactType.EMAIL)
                                                  .map(ContactLog::getExpertId).collect(Collectors.toSet());
            phoneViewedExperts = viewerContactLogs.stream()
                                                  .filter(log -> log.getContactType() == ContactType.PHONE)
                                                  .map(ContactLog::getExpertId).collect(Collectors.toSet());
        }

        Set<Long> finalEmailViewedExperts = emailViewedExperts;
        Set<Long> finalPhoneViewedExperts = phoneViewedExperts;
        List<ProfileItem> content = users.stream().map(user -> convertToProfileItem(user,
                userSpecialtyMap.getOrDefault(user.getId(), List.of()), finalEmailViewedExperts.contains(user.getId()),
                finalPhoneViewedExperts.contains(user.getId()))).collect(Collectors.toList());

        return new ProfileListResponse(content, hasNext);
    }

    @Transactional
    public ContactViewResponse viewContact(Long viewerId, Long expertId, ContactType contactType) {
        User expert = userRepository.findById(expertId)
                                    .orElseThrow(() -> new IllegalArgumentException("전문가를 찾을 수 없습니다."));

        // ContactLog 기록 (중복 방지)
        contactLogRepository.findByViewerIdAndExpertIdAndContactType(viewerId, expertId, contactType).orElseGet(() -> {
            ContactLog contactLog = ContactLog.builder().viewerId(viewerId).expertId(expertId).contactType(contactType)
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


    private ProfileItem convertToProfileItem(User user, List<UserSpecialty> userSpecialties, boolean isEmailViewed,
                                             boolean isPhoneViewed) {
        List<SpecialtyInfo> specialties = userSpecialties.stream().map(us -> {
            var detail = specialtyService.getDetail(us.getSpecialtyDetailId());
            return new SpecialtyInfo(detail.name(), us.getHourlyRate(), us.getSpecialtyDetailId());
        }).collect(Collectors.toList());

        return new ProfileItem(user.getId(), user.getName(), user.getGender(), user.getRegion(), specialties,
                user.getIntroduction(), user.getAverageRating(), user.getReviewCount(),
                StringUtils.hasText(user.getEmail()), StringUtils.hasText(user.getPhone()), isEmailViewed,
                isPhoneViewed, isEmailViewed ? user.getEmail() : null, isPhoneViewed ? user.getPhone() : null,
                isPhoneViewed ? user.getPhoneMessage() : null

        );
    }
}
