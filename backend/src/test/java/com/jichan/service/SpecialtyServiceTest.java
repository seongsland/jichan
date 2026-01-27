package com.jichan.service;

import com.jichan.dto.SpecialtyDto;
import com.jichan.entity.SpecialtyCategory;
import com.jichan.entity.SpecialtyDetail;
import com.jichan.repository.SpecialtyCategoryRepository;
import com.jichan.repository.SpecialtyDetailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SpecialtyServiceTest {

    @InjectMocks
    private SpecialtyService specialtyService;

    @Mock
    private SpecialtyCategoryRepository      specialtyCategoryRepository;
    @Mock
    private SpecialtyDetailRepository        specialtyDetailRepository;
    @Mock
    private ObjectProvider<SpecialtyService> selfProvider;

    @Test
    @DisplayName("카테고리 목록 조회 성공")
    void getCategories_success() {
        // given
        SpecialtyCategory category = SpecialtyCategory.builder()
                .id(1L)
                .name("Category")
                .build();
        given(specialtyCategoryRepository.findAll(any(Sort.class))).willReturn(List.of(category));

        // when
        List<SpecialtyDto.CategoryResponse> responses = specialtyService.getCategories();

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0)
                .name()).isEqualTo("Category");
    }

    @Test
    @DisplayName("상세 분야 목록 조회 성공")
    void getDetails_success() {
        // given
        SpecialtyCategory category = SpecialtyCategory.builder()
                .id(1L)
                .name("Category")
                .build();
        SpecialtyDetail   detail   = SpecialtyDetail.builder()
                .id(1L)
                .name("Detail")
                .category(category)
                .build();
        given(specialtyDetailRepository.findAll(any(Sort.class))).willReturn(List.of(detail));

        // when
        List<SpecialtyDto.DetailResponse> responses = specialtyService.getDetails();

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0)
                .name()).isEqualTo("Detail");
    }

    @Test
    @DisplayName("단일 카테고리 조회 성공")
    void getCategory_success() {
        // given
        Long categoryId = 1L;
        given(selfProvider.getObject()).willReturn(specialtyService);
        given(specialtyCategoryRepository.findAll(any(Sort.class)))
                .willReturn(List.of(SpecialtyCategory.builder()
                        .id(categoryId)
                        .name("Category")
                        .build()));

        // when
        SpecialtyDto.CategoryResponse response = specialtyService.getCategory(categoryId);

        // then
        assertThat(response.id()).isEqualTo(categoryId);
    }

    @Test
    @DisplayName("단일 카테고리 조회 실패")
    void getCategory_fail() {
        // given
        Long categoryId = 1L;
        given(selfProvider.getObject()).willReturn(specialtyService);
        given(specialtyCategoryRepository.findAll(any(Sort.class))).willReturn(List.of());

        // when & then
        assertThatThrownBy(() -> specialtyService.getCategory(categoryId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Specialty category not found");
    }

    @Test
    @DisplayName("단일 상세 분야 조회 성공")
    void getDetail_success() {
        // given
        Long              detailId = 1L;
        SpecialtyCategory category = SpecialtyCategory.builder()
                .id(1L)
                .name("Category")
                .build();
        SpecialtyDetail   detail   = SpecialtyDetail.builder()
                .id(detailId)
                .name("Detail")
                .category(category)
                .build();

        given(selfProvider.getObject()).willReturn(specialtyService);
        given(specialtyDetailRepository.findAll(any(Sort.class))).willReturn(List.of(detail));

        // when
        SpecialtyDto.DetailResponse response = specialtyService.getDetail(detailId);

        // then
        assertThat(response.id()).isEqualTo(detailId);
    }

    @Test
    @DisplayName("단일 상세 분야 조회 실패")
    void getDetail_fail() {
        // given
        Long detailId = 1L;
        given(selfProvider.getObject()).willReturn(specialtyService);
        given(specialtyDetailRepository.findAll(any(Sort.class))).willReturn(List.of());

        // when & then
        assertThatThrownBy(() -> specialtyService.getDetail(detailId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Specialty detail not found");
    }
}
