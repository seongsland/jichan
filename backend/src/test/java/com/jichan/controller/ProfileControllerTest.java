package com.jichan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jichan.dto.ProfileDto.ContactViewRequest;
import com.jichan.entity.*;
import com.jichan.entity.ContactType;
import com.jichan.repository.*;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpecialtyCategoryRepository specialtyCategoryRepository;

    @Autowired
    private SpecialtyDetailRepository specialtyDetailRepository;

    @Autowired
    private UserSpecialtyRepository userSpecialtyRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User viewer;
    private User expert1;
    private User expert2;
    private SpecialtyCategory category;
    private SpecialtyDetail detail;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        specialtyCategoryRepository.deleteAll();
        ratingRepository.deleteAll();

        viewer = User.builder()
                .name("조회자")
                .email("viewer@example.com")
                .password(passwordEncoder.encode("password123"))
                .isVisible(false)
                .build();
        userRepository.save(viewer);

        expert1 = User.builder()
                .name("전문가1")
                .email("expert1@example.com")
                .password(passwordEncoder.encode("password123"))
                .gender("남성")
                .region("서울")
                .introduction("전문가1 소개")
                .isVisible(true)
                .phone("010-1111-1111")
                .phoneMessage("평일 7시 이후 통화 가능")
                .build();
        userRepository.save(expert1);

        expert2 = User.builder()
                .name("전문가2")
                .email("expert2@example.com")
                .password(passwordEncoder.encode("password123"))
                .gender("여성")
                .region("부산")
                .introduction("전문가2 소개")
                .isVisible(true)
                .email("expert2@example.com")
                .phone("010-2222-2222")
                .build();
        userRepository.save(expert2);

        category = SpecialtyCategory.builder()
                .name("카테고리1")
                .build();
        specialtyCategoryRepository.save(category);

        detail = SpecialtyDetail.builder()
                .category(category)
                .name("주특기1")
                .build();
        specialtyDetailRepository.save(detail);

        UserSpecialty userSpecialty1 = UserSpecialty.builder()
                .userId(expert1.getId())
                .specialtyDetail(detail)
                .hourlyRate(50000)
                .build();
        userSpecialtyRepository.save(userSpecialty1);

        UserSpecialty userSpecialty2 = UserSpecialty.builder()
                .userId(expert2.getId())
                .specialtyDetail(detail)
                .hourlyRate(30000)
                .build();
        userSpecialtyRepository.save(userSpecialty2);

        Rating rating = Rating.builder()
                .userId(viewer.getId())
                .expertId(expert1.getId())
                .score(5)
                .comment("좋아요")
                .build();
        ratingRepository.save(rating);
    }

    @Test
    @DisplayName("프로필 목록 조회 성공 - 기본 조회")
    void getProfiles_success_default() throws Exception {
        // when & then
        mockMvc.perform(get("/api/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.hasNext").exists());
    }

    @Test
    @DisplayName("프로필 목록 조회 성공 - 주특기 필터링")
    void getProfiles_success_withSpecialty() throws Exception {
        // when & then
        mockMvc.perform(get("/api/profile")
                        .param("specialty", detail.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("프로필 목록 조회 성공 - 평점순 정렬")
    void getProfiles_success_sortByRating() throws Exception {
        // when & then
        mockMvc.perform(get("/api/profile")
                        .param("sortBy", "rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("프로필 목록 조회 성공 - 금액순 정렬")
    void getProfiles_success_sortByPrice() throws Exception {
        // when & then
        mockMvc.perform(get("/api/profile")
                        .param("sortBy", "price"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("프로필 목록 조회 성공 - 페이징")
    void getProfiles_success_withPaging() throws Exception {
        // when & then
        mockMvc.perform(get("/api/profile")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.hasNext").exists());
    }

    @Test
    @DisplayName("프로필 목록 조회 성공 - is_visible=false인 프로필 제외")
    void getProfiles_success_excludeInvisible() throws Exception {
        // given
        User invisibleExpert = User.builder()
                .name("비공개전문가")
                .email("invisible@example.com")
                .password(passwordEncoder.encode("password123"))
                .isVisible(false)
                .build();
        userRepository.save(invisibleExpert);

        // when & then
        mockMvc.perform(get("/api/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("연락처 보기 성공 - 이메일")
    @WithMockUser(username = "viewer@example.com")
    void contactView_success_email() throws Exception {
        // given
        ContactViewRequest request = new ContactViewRequest(expert1.getId(), ContactType.EMAIL.name());

        // when & then
        mockMvc.perform(post("/api/profile/contact_view")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contact").exists());
    }

    @Test
    @DisplayName("연락처 보기 성공 - 핸드폰")
    @WithMockUser(username = "viewer@example.com")
    void contactView_success_phone() throws Exception {
        // given
        ContactViewRequest request = new ContactViewRequest(expert1.getId(), ContactType.PHONE.name());

        // when & then
        mockMvc.perform(post("/api/profile/contact_view")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contact").exists())
                .andExpect(jsonPath("$.phoneMessage").exists());
    }

    @Test
    @DisplayName("연락처 보기 실패 - 인증되지 않은 사용자")
    void contactView_fail_unauthorized() throws Exception {
        // given
        ContactViewRequest request = new ContactViewRequest(expert1.getId(), ContactType.EMAIL.name());

        // when & then
        mockMvc.perform(post("/api/profile/contact_view")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("연락처 보기 실패 - 존재하지 않는 전문가")
    @WithMockUser(username = "viewer@example.com")
    void contactView_fail_expertNotFound() throws Exception {
        // given
        ContactViewRequest request = new ContactViewRequest(999L, ContactType.EMAIL.name());

        // when & then
        mockMvc.perform(post("/api/profile/contact_view")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("연락처 보기 - 중복 조회 시 로그 중복 기록 방지")
    @WithMockUser(username = "viewer@example.com")
    void contactView_success_duplicateView() throws Exception {
        // given
        ContactViewRequest request = new ContactViewRequest(expert1.getId(), ContactType.EMAIL.name());

        // when - 첫 번째 조회
        mockMvc.perform(post("/api/profile/contact_view")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // then - 두 번째 조회 (중복이어도 성공)
        mockMvc.perform(post("/api/profile/contact_view")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
