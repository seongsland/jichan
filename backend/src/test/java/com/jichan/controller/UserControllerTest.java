package com.jichan.controller;

import com.jichan.dto.UserDto.ProfileResponse;
import com.jichan.dto.UserDto.ProfileUpdateRequest;
import com.jichan.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest extends ControllerTestSupport {

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("프로필 조회 성공")
    void getProfile_success() throws Exception {
        // given
        ProfileResponse response = new ProfileResponse(1L, "test", "Male", "Seoul", "Hello", false, null, null,
                Collections.emptyList());
        given(userService.getProfile(1L)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/user/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    @DisplayName("프로필 수정 성공")
    void updateProfile_success() throws Exception {
        // given
        ProfileUpdateRequest request = new ProfileUpdateRequest("newName", "Male", "Seoul", "Hello", false, null, null,
                null);
        ProfileResponse response = new ProfileResponse(1L, "newName", "Male", "Seoul", "Hello", false, null, null,
                Collections.emptyList());
        given(userService.updateProfile(eq(1L), any(ProfileUpdateRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(put("/api/user/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("newName"));
    }
}
