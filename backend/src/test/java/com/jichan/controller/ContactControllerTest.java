package com.jichan.controller;

import com.jichan.dto.ContactDto;
import com.jichan.dto.ContactDto.ContactSliceResponse;
import com.jichan.dto.ContactDto.RatingRequest;
import com.jichan.dto.ContactDto.RatingResponse;
import com.jichan.service.ContactService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactController.class)
class ContactControllerTest extends ControllerTestSupport {

    @MockBean
    private ContactService contactService;

    @Test
    @DisplayName("연락처 목록 조회 성공")
    void getContacts_success() throws Exception {
        // given
        var contactListResponse = new ContactDto.ContactListResponse(2L, "홍길동", "남", "", null, "String introduction",
                true, true, 5, "String email", "String phone", "String phoneMessage");
        ContactSliceResponse response = new ContactSliceResponse(List.of(contactListResponse), false);
        given(contactService.getContacts(anyLong(), any(), any(), anyInt())).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/contact").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasNext").value(false));
    }

    @Test
    @DisplayName("평가 생성/수정 성공")
    void createOrUpdateRating_success() throws Exception {
        // given
        RatingRequest  request  = new RatingRequest(2L, 5);
        RatingResponse response = new RatingResponse(1L, 2L, 5);
        given(contactService.createOrUpdateRating(eq(1L), any(RatingRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/contact/rating").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score").value(5));
    }

    @Test
    @DisplayName("연락처 삭제 성공")
    void deleteContact_success() throws Exception {
        // given
        doNothing().when(contactService)
                .deleteContact(1L, 2L);

        // when & then
        mockMvc.perform(delete("/api/contact/2"))
                .andExpect(status().isNoContent());

        verify(contactService).deleteContact(1L, 2L);
    }
}
