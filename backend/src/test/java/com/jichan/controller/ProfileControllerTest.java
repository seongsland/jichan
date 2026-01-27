package com.jichan.controller;

import com.jichan.dto.ProfileDto.ContactViewRequest;
import com.jichan.dto.ProfileDto.ContactViewResponse;
import com.jichan.dto.ProfileDto.ProfileListResponse;
import com.jichan.dto.ProfileDto.ProfileRequest;
import com.jichan.entity.ContactType;
import com.jichan.service.ProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest extends ControllerTestSupport {

    @MockBean
    private ProfileService profileService;

    @Test
    @DisplayName("프로필 목록 조회 성공")
    @WithMockUser(username = "1")
    void getProfiles_success() throws Exception {
        // given
        ProfileListResponse response = new ProfileListResponse(Collections.emptyList(), false);
        given(profileService.getProfiles(eq(1L), any(ProfileRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/profile").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasNext").value(false));
    }

    @Test
    @DisplayName("연락처 조회 성공")
    @WithMockUser(username = "1")
    void viewContact_success() throws Exception {
        // given
        ContactViewRequest  request  = new ContactViewRequest(2L, "EMAIL");
        ContactViewResponse response = new ContactViewResponse("test@example.com", null);
        given(profileService.viewContact(1L, 2L, ContactType.EMAIL)).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/profile/contact_view").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contact").value("test@example.com"));
    }
}
