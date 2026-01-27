package com.jichan.controller;

import com.jichan.dto.SpecialtyDto;
import com.jichan.service.SpecialtyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpecialtyController.class)
class SpecialtyControllerTest extends ControllerTestSupport {

    @MockBean
    private SpecialtyService specialtyService;

    @Test
    @DisplayName("카테고리 목록 조회 성공")
    void getCategories_success() throws Exception {
        // given
        List<SpecialtyDto.CategoryResponse> responses = List.of(new SpecialtyDto.CategoryResponse(1L, "Category"));
        given(specialtyService.getCategories()).willReturn(responses);

        // when & then
        mockMvc.perform(get("/api/specialty/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Category"));
    }

    @Test
    @DisplayName("상세 분야 목록 조회 성공")
    void getDetails_success() throws Exception {
        // given
        List<SpecialtyDto.DetailResponse> responses = List.of(new SpecialtyDto.DetailResponse(1L, "Detail", 1L));
        given(specialtyService.getDetails()).willReturn(responses);

        // when & then
        mockMvc.perform(get("/api/specialty/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Detail"));
    }
}
