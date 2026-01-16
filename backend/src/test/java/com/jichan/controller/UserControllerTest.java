package com.jichan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jichan.dto.UserDto.ProfileUpdateRequest;
import com.jichan.entity.User;
import com.jichan.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        testUser = User.builder()
                .name("홍길동")
                .email("test@example.com")
                .password(passwordEncoder.encode("password123"))
                .gender("남성")
                .region("서울")
                .introduction("소개글")
                .isVisible(false)
                .build();
        userRepository.save(testUser);
    }

    @Test
    @DisplayName("프로필 업데이트 성공")
    @WithMockUser(username = "test@example.com")
    void updateProfile_success() throws Exception {
        // given
        ProfileUpdateRequest request = new ProfileUpdateRequest("김철수", "여성", "부산", "수정된 소개글", true, "010-1234-5678", "평일 7시 이후 통화 가능");

        // when & then
        mockMvc.perform(put("/api/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("김철수"))
                .andExpect(jsonPath("$.isVisible").value(true));
    }

    @Test
    @DisplayName("프로필 업데이트 성공 - 일부 필드만 업데이트")
    @WithMockUser(username = "test@example.com")
    void updateProfile_success_partialUpdate() throws Exception {
        // given
        ProfileUpdateRequest request = new ProfileUpdateRequest(null, null, null, null, true, null, null);

        // when & then
        mockMvc.perform(put("/api/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isVisible").value(true));
    }

    @Test
    @DisplayName("프로필 업데이트 실패 - 인증되지 않은 사용자")
    void updateProfile_fail_unauthorized() throws Exception {
        // given
        ProfileUpdateRequest request = new ProfileUpdateRequest("김철수", null, null, null, null, null, null);

        // when & then
        mockMvc.perform(put("/api/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("프로필 노출 여부 변경 성공")
    @WithMockUser(username = "test@example.com")
    void updateProfile_success_visibilityChange() throws Exception {
        // given
        ProfileUpdateRequest request = new ProfileUpdateRequest(null, null, null, null, true, null, null);

        // when & then
        mockMvc.perform(put("/api/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isVisible").value(true));
    }
}
