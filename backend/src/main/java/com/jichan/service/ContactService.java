package com.jichan.service;

import com.jichan.dto.ContactDto.*;
import com.jichan.dto.SpecialtyDto;
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
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactService {

    private static final int PAGE_SIZE = 10;
    private final ContactLogRepository contactLogRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final UserSpecialtyRepository userSpecialtyRepository;
    private final SpecialtyService specialtyService;

    public ContactSliceResponse getContacts(Long viewerId, Long categoryId, Long specialtyDetailId, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        // 1. QueryDSL을 사용하여 필터링 및 페이징된 전문가 ID 목록 조회
        Slice<Long> expertIdSlice = contactLogRepository.findExpertIdsByViewerIdAndFilters(viewerId, categoryId,
                specialtyDetailId, pageable);

        List<Long> expertIds = expertIdSlice.getContent();

        if (CollectionUtils.isEmpty(expertIds)) {
            return new ContactSliceResponse(List.of(), false);
        }

        // 2. 필요한 데이터를 한 번의 쿼리로 가져옴
        Map<Long, User> expertMap = userRepository.findAllById(expertIds).stream()
                                                  .collect(Collectors.toMap(User::getId, u -> u));

        Map<Long, Rating> ratingMap = ratingRepository.findByUserIdAndExpertIdIn(viewerId, expertIds).stream()
                                                      .collect(Collectors.toMap(Rating::getExpertId, r -> r));

        Map<Long, List<ContactLog>> contactLogMap = contactLogRepository.findByViewerIdAndExpertIdIn(viewerId,
                expertIds).stream().collect(Collectors.groupingBy(ContactLog::getExpertId));

        Map<Long, List<UserSpecialty>> userSpecialtyMap = userSpecialtyRepository.findByUserIdIn(expertIds).stream()
                                                                                 .collect(Collectors.groupingBy(
                                                                                         UserSpecialty::getUserId));

        // 3. DTO로 변환
        List<ContactListResponse> content = expertIds.stream().map(expertId -> {
            User expert = expertMap.get(expertId);
            Rating rating = ratingMap.get(expertId);
            List<ContactLog> logs = contactLogMap.get(expertId);
            List<UserSpecialty> userSpecialties = userSpecialtyMap.getOrDefault(expertId, List.of());

            boolean hasEmailView = logs != null && logs.stream().anyMatch(l -> l.getContactType() == ContactType.EMAIL);
            boolean hasPhoneView = logs != null && logs.stream().anyMatch(l -> l.getContactType() == ContactType.PHONE);

            List<SpecialtyInfo> specialties = userSpecialties.stream().map(us -> {
                SpecialtyDto.DetailResponse detail = specialtyService.getDetail(us.getSpecialtyDetailId());
                return new SpecialtyInfo(detail.name(), us.getHourlyRate(), us.getSpecialtyDetailId());
            }).collect(Collectors.toList());

            return new ContactListResponse(expert.getId(), expert.getName(), expert.getGender(), expert.getRegion(),
                    specialties, expert.getIntroduction(), hasEmailView, hasPhoneView,
                    rating != null ? rating.getScore() : null, hasEmailView ? expert.getEmail() : null,
                    hasPhoneView ? expert.getPhone() : null, hasPhoneView ? expert.getPhoneMessage() : null);
        }).collect(Collectors.toList());

        return new ContactSliceResponse(content, expertIdSlice.hasNext());
    }

    @Transactional
    public RatingResponse createOrUpdateRating(Long userId, RatingRequest request) {
        Optional<Rating> existingRatingOpt = ratingRepository.findByUserIdAndExpertId(userId, request.expertId());

        Rating rating;
        if (existingRatingOpt.isPresent()) {
            rating = existingRatingOpt.get();
            rating.update(request.score());
        } else {
            rating = Rating.builder().userId(userId).expertId(request.expertId()).score(request.score()).build();
        }
        rating = ratingRepository.save(rating);
        updateExpertRatingStats(request.expertId());
        return new RatingResponse(rating.getId(), request.expertId(), rating.getScore());
    }

    @Transactional
    public void deleteContact(Long viewerId, Long expertId) {
        contactLogRepository.deleteByViewerIdAndExpertId(viewerId, expertId);
        ratingRepository.deleteByUserIdAndExpertId(viewerId, expertId);
        updateExpertRatingStats(expertId);
    }

    private void updateExpertRatingStats(Long expertId) {
        User expert = userRepository.findById(expertId)
                                    .orElseThrow(() -> new IllegalArgumentException("전문가를 찾을 수 없습니다."));

        RatingStatsDto stats = ratingRepository.findStatsByExpertId(expertId);
        expert.updateRating(stats.average());
        expert.updateReviewCount(stats.count().intValue());
        userRepository.save(expert);
    }
}
