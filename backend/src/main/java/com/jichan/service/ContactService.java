package com.jichan.service;

import com.jichan.dto.ContactDto.ContactListResponse;
import com.jichan.dto.ContactDto.RatingRequest;
import com.jichan.dto.ContactDto.RatingResponse;
import com.jichan.entity.ContactLog;
import com.jichan.entity.ContactType;
import com.jichan.entity.Rating;
import com.jichan.entity.User;
import com.jichan.repository.ContactLogRepository;
import com.jichan.repository.RatingRepository;
import com.jichan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactService {

    private final ContactLogRepository contactLogRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public List<ContactListResponse> getContacts(Long viewerId) {
        List<ContactLog> contactLogs = contactLogRepository.findByViewerId(viewerId);

        return new ArrayList<>(contactLogs.stream()
                .map(log -> {
                    User expert = userRepository.findById(log.getExpertId()).orElse(null);
                    Rating rating = ratingRepository.findByUserIdAndExpertId(viewerId, log.getExpertId()).orElse(null);

                    boolean hasEmailView = contactLogRepository.findByViewerIdAndExpertIdAndContactType(
                            viewerId, log.getExpertId(), ContactType.EMAIL).isPresent();
                    boolean hasPhoneView = contactLogRepository.findByViewerIdAndExpertIdAndContactType(
                            viewerId, log.getExpertId(), ContactType.PHONE).isPresent();

                    return new ContactListResponse(
                            expert != null ? expert.getId() : log.getExpertId(),
                            expert != null ? expert.getName() : "Unknown",
                            expert != null ? expert.getGender() : null,
                            expert != null ? expert.getRegion() : null,
                            expert != null ? expert.getIntroduction() : null,
                            hasEmailView,
                            hasPhoneView,
                            rating != null ? rating.getScore() : null
                    );
                })
                .collect(Collectors.toMap(
                        ContactListResponse::expertId,
                        r -> r,
                        (r1, r2) -> r1
                ))
                .values());
    }

    @Transactional
    public RatingResponse createRating(Long userId, RatingRequest request) {
        if (ratingRepository.existsByUserIdAndExpertId(userId, request.expertId())) {
            throw new IllegalStateException("이미 평가한 전문가입니다.");
        }

        Rating rating = Rating.builder()
                .userId(userId)
                .expertId(request.expertId())
                .score(request.score())
                .comment(request.comment())
                .build();

        rating = ratingRepository.save(rating);

        // Update expert's rating stats
        User expert = userRepository.findById(request.expertId())
                .orElseThrow(() -> new IllegalArgumentException("전문가를 찾을 수 없습니다."));
        List<Rating> expertRatings = ratingRepository.findByExpertId(request.expertId());
        int reviewCount = expertRatings.size();
        int averageRating = (int) Math.round(expertRatings.stream().mapToInt(Rating::getScore).average().orElse(0.0));
        expert.updateRating(averageRating, reviewCount);
        userRepository.save(expert);

        return new RatingResponse(
                rating.getId(),
                request.expertId(),
                rating.getScore(),
                rating.getComment()
        );
    }
}
