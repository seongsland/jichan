package com.jichan.service;

import com.jichan.dto.SpecialtyDto;
import com.jichan.dto.UserDto.ProfileResponse;
import com.jichan.dto.UserDto.ProfileUpdateRequest;
import com.jichan.dto.UserDto.SpecialtyRequest;
import com.jichan.entity.User;
import com.jichan.entity.UserSpecialty;
import com.jichan.repository.UserRepository;
import com.jichan.repository.UserSpecialtyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository          userRepository;
    @Mock
    private UserSpecialtyRepository userSpecialtyRepository;
    @Mock
    private SpecialtyService        specialtyService;

    @Test
    @DisplayName("프로필 조회 성공")
    void getProfile_success() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .name("test")
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userSpecialtyRepository.findByUserId(userId)).willReturn(Collections.emptyList());

        // when
        ProfileResponse response = userService.getProfile(userId);

        // then
        assertThat(response.id()).isEqualTo(userId);
        assertThat(response.name()).isEqualTo("test");
    }

    @Test
    @DisplayName("프로필 조회 실패 - 사용자 없음")
    void getProfile_fail_user_not_found() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getProfile(userId))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("프로필 수정 성공")
    void updateProfile_success() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .build();
        ProfileUpdateRequest request = new ProfileUpdateRequest(
                "newName", "Male", "Seoul", "Hello", false, null, null, null
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        ProfileResponse response = userService.updateProfile(userId, request);

        // then
        assertThat(response.name()).isEqualTo("newName");
        assertThat(response.gender()).isEqualTo("Male");
        verify(userSpecialtyRepository).deleteAllByUserId(userId);
    }

    @Test
    @DisplayName("프로필 공개 설정 실패 - 필수 정보 누락")
    void updateProfile_fail_missing_info() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .build();
        ProfileUpdateRequest request = new ProfileUpdateRequest(
                null, "Male", "Seoul", "Hello", true, null, null, null
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> userService.updateProfile(userId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("프로필을 공개하려면 이름, 성별, 지역, 소개글을 모두 입력해야 합니다.");
    }

    @Test
    @DisplayName("프로필 공개 설정 실패 - 특기 미등록")
    void updateProfile_fail_no_specialties() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .build();
        ProfileUpdateRequest request = new ProfileUpdateRequest(
                "Name", "Male", "Seoul", "Hello", true, null, null, Collections.emptyList()
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> userService.updateProfile(userId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("프로필을 공개하려면 최소 1개 이상의 특기를 등록해야 합니다.");
    }

    @Test
    @DisplayName("프로필 수정 성공 - 특기 포함")
    void updateProfile_success_with_specialties() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .build();
        List<SpecialtyRequest> specialtyRequests = List.of(new SpecialtyRequest(1L, 10000));
        ProfileUpdateRequest request = new ProfileUpdateRequest(
                "Name", "Male", "Seoul", "Hello", true, null, null, specialtyRequests
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(specialtyService.getDetail(anyLong())).willReturn(new SpecialtyDto.DetailResponse(1L, "Detail", 1L));
        given(specialtyService.getCategory(anyLong())).willReturn(new SpecialtyDto.CategoryResponse(1L, "Category"));

        // when
        ProfileResponse response = userService.updateProfile(userId, request);

        // then
        verify(userSpecialtyRepository).save(any(UserSpecialty.class));
        assertThat(response.specialties()).hasSize(1);
        assertThat(response.specialties()
                .get(0)
                .hourlyRate()).isEqualTo(10000);
    }
}
