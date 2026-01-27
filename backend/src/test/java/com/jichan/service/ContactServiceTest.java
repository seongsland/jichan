package com.jichan.service;

import com.jichan.dto.ContactDto.ContactSliceResponse;
import com.jichan.dto.ContactDto.RatingRequest;
import com.jichan.dto.ContactDto.RatingResponse;
import com.jichan.dto.ContactDto.RatingStatsDto;
import com.jichan.entity.Rating;
import com.jichan.entity.User;
import com.jichan.repository.ContactLogRepository;
import com.jichan.repository.RatingRepository;
import com.jichan.repository.UserRepository;
import com.jichan.repository.UserSpecialtyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @InjectMocks
    private ContactService contactService;

    @Mock
    private ContactLogRepository    contactLogRepository;
    @Mock
    private UserRepository          userRepository;
    @Mock
    private RatingRepository        ratingRepository;
    @Mock
    private UserSpecialtyRepository userSpecialtyRepository;

    @Test
    @DisplayName("연락처 목록 조회 성공")
    void getContacts_success() {
        // given
        Long viewerId = 1L;
        Long expertId = 2L;
        given(contactLogRepository.findExpertIdsByViewerIdAndFilters(anyLong(), any(), any(), any(Pageable.class)))
                .willReturn(new SliceImpl<>(List.of(expertId)));

        User expert = User.builder()
                .id(expertId)
                .name("Expert")
                .build();
        given(userRepository.findAllById(List.of(expertId))).willReturn(List.of(expert));
        given(ratingRepository.findByUserIdAndExpertIdIn(viewerId, List.of(expertId))).willReturn(
                Collections.emptyList());
        given(contactLogRepository.findByViewerIdAndExpertIdIn(viewerId, List.of(expertId))).willReturn(
                Collections.emptyList());
        given(userSpecialtyRepository.findByUserIdIn(List.of(expertId))).willReturn(Collections.emptyList());

        // when
        ContactSliceResponse response = contactService.getContacts(viewerId, null, null, 0);

        // then
        assertThat(response.content()).hasSize(1);
        assertThat(response.content()
                .get(0)
                .expertName()).isEqualTo("Expert");
    }

    @Test
    @DisplayName("연락처 목록 조회 - 데이터 없음")
    void getContacts_empty() {
        // given
        given(contactLogRepository.findExpertIdsByViewerIdAndFilters(anyLong(), any(), any(), any(Pageable.class)))
                .willReturn(new SliceImpl<>(Collections.emptyList()));

        // when
        ContactSliceResponse response = contactService.getContacts(1L, null, null, 0);

        // then
        assertThat(response.content()).isEmpty();
    }

    @Test
    @DisplayName("평가 생성 성공")
    void createOrUpdateRating_create_success() {
        // given
        Long          userId   = 1L;
        Long          expertId = 2L;
        RatingRequest request  = new RatingRequest(expertId, 5);

        given(ratingRepository.findByUserIdAndExpertId(userId, expertId)).willReturn(Optional.empty());
        given(ratingRepository.save(any(Rating.class))).willAnswer(invocation -> {
            Rating r = invocation.getArgument(0);
            return Rating.builder()
                    .id(1L)
                    .userId(r.getUserId())
                    .expertId(r.getExpertId())
                    .score(r.getScore())
                    .build();
        });
        given(userRepository.findById(expertId)).willReturn(Optional.of(User.builder()
                .id(expertId)
                .build()));
        given(ratingRepository.findStatsByExpertId(expertId)).willReturn(new RatingStatsDto(5.0, 1L));

        // when
        RatingResponse response = contactService.createOrUpdateRating(userId, request);

        // then
        assertThat(response.score()).isEqualTo(5);
        verify(ratingRepository).save(any(Rating.class));
    }

    @Test
    @DisplayName("평가 수정 성공")
    void createOrUpdateRating_update_success() {
        // given
        Long          userId         = 1L;
        Long          expertId       = 2L;
        RatingRequest request        = new RatingRequest(expertId, 4);
        Rating        existingRating = Rating.builder()
                .id(1L)
                .userId(userId)
                .expertId(expertId)
                .score(5)
                .build();

        given(ratingRepository.findByUserIdAndExpertId(userId, expertId)).willReturn(Optional.of(existingRating));
        given(ratingRepository.save(any(Rating.class))).willReturn(existingRating);
        given(userRepository.findById(expertId)).willReturn(Optional.of(User.builder()
                .id(expertId)
                .build()));
        given(ratingRepository.findStatsByExpertId(expertId)).willReturn(new RatingStatsDto(4.0, 1L));

        // when
        RatingResponse response = contactService.createOrUpdateRating(userId, request);

        // then
        assertThat(response.score()).isEqualTo(4);
    }

    @Test
    @DisplayName("연락처 삭제 성공")
    void deleteContact_success() {
        // given
        Long viewerId = 1L;
        Long expertId = 2L;
        given(userRepository.findById(expertId)).willReturn(Optional.of(User.builder()
                .id(expertId)
                .build()));
        given(ratingRepository.findStatsByExpertId(expertId)).willReturn(new RatingStatsDto(0.0, 0L));

        // when
        contactService.deleteContact(viewerId, expertId);

        // then
        verify(contactLogRepository).deleteByViewerIdAndExpertId(viewerId, expertId);
        verify(ratingRepository).deleteByUserIdAndExpertId(viewerId, expertId);
    }
}
