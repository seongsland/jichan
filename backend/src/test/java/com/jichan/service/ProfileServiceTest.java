package com.jichan.service;

import com.jichan.dto.ProfileDto.ContactViewResponse;
import com.jichan.dto.ProfileDto.ProfileListResponse;
import com.jichan.dto.ProfileDto.ProfileRequest;
import com.jichan.entity.ContactLog;
import com.jichan.entity.ContactType;
import com.jichan.entity.User;
import com.jichan.repository.ContactLogRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @InjectMocks
    private ProfileService profileService;

    @Mock
    private UserRepository          userRepository;
    @Mock
    private UserSpecialtyRepository userSpecialtyRepository;
    @Mock
    private ContactLogRepository    contactLogRepository;

    @Test
    @DisplayName("프로필 목록 조회 성공")
    void getProfiles_success() {
        // given
        Long           viewerId = 1L;
        ProfileRequest request  = new ProfileRequest(null, null, "rating", "Seoul", 0);
        User user = User.builder()
                .id(2L)
                .name("Expert")
                .build();

        given(userRepository.findProfiles(any(ProfileRequest.class), any(Pageable.class)))
                .willReturn(new SliceImpl<>(List.of(user)));
        given(userSpecialtyRepository.findByUserIdIn(List.of(2L))).willReturn(Collections.emptyList());
        given(contactLogRepository.findByViewerIdAndExpertIdIn(viewerId, List.of(2L))).willReturn(
                Collections.emptyList());

        // when
        ProfileListResponse response = profileService.getProfiles(viewerId, request);

        // then
        assertThat(response.content()).hasSize(1);
        assertThat(response.content()
                .get(0)
                .name()).isEqualTo("Expert");
    }

    @Test
    @DisplayName("연락처 조회 성공 - 이메일")
    void viewContact_email_success() {
        // given
        Long viewerId = 1L;
        Long expertId = 2L;
        User expert = User.builder()
                .id(expertId)
                .email("expert@example.com")
                .build();

        given(userRepository.findById(expertId)).willReturn(Optional.of(expert));
        given(contactLogRepository.findByViewerIdAndExpertIdAndContactType(viewerId, expertId, ContactType.EMAIL))
                .willReturn(Optional.empty());
        given(contactLogRepository.save(any(ContactLog.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        ContactViewResponse response = profileService.viewContact(viewerId, expertId, ContactType.EMAIL);

        // then
        assertThat(response.contact()).isEqualTo("expert@example.com");
        verify(contactLogRepository).save(any(ContactLog.class));
    }

    @Test
    @DisplayName("연락처 조회 성공 - 전화번호")
    void viewContact_phone_success() {
        // given
        Long viewerId = 1L;
        Long expertId = 2L;
        User expert = User.builder()
                .id(expertId)
                .phone("010-1234-5678")
                .phoneMessage("Call me")
                .build();

        given(userRepository.findById(expertId)).willReturn(Optional.of(expert));
        given(contactLogRepository.findByViewerIdAndExpertIdAndContactType(viewerId, expertId, ContactType.PHONE))
                .willReturn(Optional.empty());
        given(contactLogRepository.save(any(ContactLog.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        ContactViewResponse response = profileService.viewContact(viewerId, expertId, ContactType.PHONE);

        // then
        assertThat(response.contact()).isEqualTo("010-1234-5678");
        assertThat(response.phoneMessage()).isEqualTo("Call me");
        verify(contactLogRepository).save(any(ContactLog.class));
    }
}
