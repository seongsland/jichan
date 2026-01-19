package com.jichan.service;

import com.jichan.dto.SpecialtyDto;
import com.jichan.repository.SpecialtyCategoryRepository;
import com.jichan.repository.SpecialtyDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpecialtyService {

    private final SpecialtyCategoryRepository specialtyCategoryRepository;
    private final SpecialtyDetailRepository specialtyDetailRepository;

    @Cacheable("categories")
    public List<SpecialtyDto.CategoryResponse> getCategories() {
        return specialtyCategoryRepository.findAll().stream()
                .map(category -> new SpecialtyDto.CategoryResponse(category.getId(), category.getName()))
                .toList();
    }

    @Cacheable("details")
    public List<SpecialtyDto.DetailResponse> getDetails() {
        return specialtyDetailRepository.findAll().stream()
                .map(detail -> new SpecialtyDto.DetailResponse(detail.getId(), detail.getName(), detail.getCategory().getId()))
                .toList();
    }

    @Cacheable("detail")
    public SpecialtyDto.DetailResponse getDetail(Long id) {
        return specialtyDetailRepository.findById(id)
                .map(detail -> new SpecialtyDto.DetailResponse(detail.getId(), detail.getName(), detail.getCategory().getId()))
                .orElseThrow(() -> new IllegalArgumentException("Specialty detail not found: " + id));
    }

    @Cacheable("category")
    public SpecialtyDto.CategoryResponse getCategory(Long id) {
        return specialtyCategoryRepository.findById(id)
                .map(category -> new SpecialtyDto.CategoryResponse(category.getId(), category.getName()))
                .orElseThrow(() -> new IllegalArgumentException("Specialty category not found: " + id));
    }
}
