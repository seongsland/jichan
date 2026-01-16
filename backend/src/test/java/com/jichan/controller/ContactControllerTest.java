package com.jichan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jichan.dto.ContactDto.RatingRequest;
import com.jichan.entity.ContactLog;
import com.jichan.entity.ContactType;
import com.jichan.entity.Rating;
import com.jichan.entity.User;
import com.jichan.repository.ContactLogRepository;
import com.jichan.repository.RatingRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactLogRepository contactLogRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User expert1;
    private User expert2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        contactLogRepository.deleteAll();
        ratingRepository.deleteAll();

        User viewer = User.builder()
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
                .email("expert1@example.com")
                .phone("010-1111-1111")
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

        // ContactLog 생성
        ContactLog contactLog1 = ContactLog.builder()
                .viewerId(viewer.getId())
                .expertId(expert1.getId())
                .contactType(ContactType.EMAIL)
                .build();
        contactLogRepository.save(contactLog1);

        ContactLog contactLog2 = ContactLog.builder()
                .viewerId(viewer.getId())
                .expertId(expert2.getId())
                .contactType(ContactType.PHONE)
                .build();
        contactLogRepository.save(contactLog2);

        // Rating 생성
        Rating rating = Rating.builder()
                .userId(viewer.getId())
                .expertId(expert1.getId())
                .score(5)
                .comment("좋아요")
                .build();
        ratingRepository.save(rating);
    }

    @Test
    @DisplayName("내가 본 전문가 목록 조회 성공")
    @WithMockUser(username = "viewer@example.com")
    void getContacts_success() throws Exception {
        // when & then
        mockMvc.perform(get("/api/contact"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].expertId").exists())
                .andExpect(jsonPath("$[0].expertName").exists())
                .andExpect(jsonPath("$[0].hasEmailView").exists())
                .andExpect(jsonPath("$[0].hasPhoneView").exists())
                .andExpect(jsonPath("$[0].rating").exists());
    }

    @Test
    @DisplayName("내가 본 전문가 목록 조회 실패 - 인증되지 않은 사용자")
    void getContacts_fail_unauthorized() throws Exception {
        // when & then
        mockMvc.perform(get("/api/contact"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("내가 본 전문가 목록 조회 - 별점 정보 포함")
    @WithMockUser(username = "viewer@example.com")
    void getContacts_success_withRating() throws Exception {
        // when & then
        mockMvc.perform(get("/api/contact"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.expertId == " + expert1.getId() + ")].rating").value(5));
    }

    @Test
    @DisplayName("평가 등록 성공")
    @WithMockUser(username = "viewer@example.com")
    void createRating_success() throws Exception {
        // given
        RatingRequest request = new RatingRequest(expert2.getId(), 4, "좋은 전문가입니다");

        // when & then
        mockMvc.perform(post("/api/contact/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score").value(4))
                .andExpect(jsonPath("$.comment").value("좋은 전문가입니다"));
    }

    @Test
    @DisplayName("평가 등록 성공 - 코멘트 없이")
    @WithMockUser(username = "viewer@example.com")
    void createRating_success_withoutComment() throws Exception {
        // given
        RatingRequest request = new RatingRequest(expert2.getId(), 5, null);

        // when & then
        mockMvc.perform(post("/api/contact/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score").value(5));
    }

    @Test
    @DisplayName("평가 등록 실패 - 인증되지 않은 사용자")
    void createRating_fail_unauthorized() throws Exception {
        // given
        RatingRequest request = new RatingRequest(expert2.getId(), 5, null);

        // when & then
        mockMvc.perform(post("/api/contact/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("평가 등록 실패 - 존재하지 않는 전문가")
    @WithMockUser(username = "viewer@example.com")
    void createRating_fail_expertNotFound() throws Exception {
        // given
        RatingRequest request = new RatingRequest(999L, 5, null);

        // when & then
        mockMvc.perform(post("/api/contact/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("평가 등록 실패 - 점수 범위 초과")
    @WithMockUser(username = "viewer@example.com")
    void createRating_fail_invalidScore() throws Exception {
        // given
        RatingRequest request = new RatingRequest(expert2.getId(), 6, null); // 1~5 범위 초과

        // when & then
        mockMvc.perform(post("/api/contact/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("평가 등록 실패 - 점수 범위 미만")
    @WithMockUser(username = "viewer@example.com")
    void createRating_fail_scoreTooLow() throws Exception {
        // given
        RatingRequest request = new RatingRequest(expert2.getId(), 0, null); // 1~5 범위 미만

        // when & then
        mockMvc.perform(post("/api/contact/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("평가 등록 실패 - 중복 평가")
    @WithMockUser(username = "viewer@example.com")
    void createRating_fail_duplicateRating() throws Exception {
        // given - 이미 평가가 존재함 (setUp에서 생성)
        RatingRequest request = new RatingRequest(expert1.getId(), 3, null);

        // when & then
        mockMvc.perform(post("/api/contact/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
