package com.jichan.service;

import com.jichan.dto.SpecialtyDto;
import com.jichan.repository.SpecialtyCategoryRepository;
import com.jichan.repository.SpecialtyDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpecialtyService {

    private final SpecialtyCategoryRepository specialtyCategoryRepository;
    private final SpecialtyDetailRepository specialtyDetailRepository;
    private final ObjectProvider<SpecialtyService> selfProvider;

    @Cacheable("categories")
    public List<SpecialtyDto.CategoryResponse> getCategories() {
        return specialtyCategoryRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder")).stream()
                                          .map(category -> new SpecialtyDto.CategoryResponse(category.getId(),
                                                  category.getName())).toList();
    }

    @Cacheable("details")
    public List<SpecialtyDto.DetailResponse> getDetails() {
        return specialtyDetailRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder")).stream()
                                        .map(detail -> new SpecialtyDto.DetailResponse(detail.getId(), detail.getName(),
                                                detail.getCategory().getId())).toList();
    }

    public SpecialtyDto.CategoryResponse getCategory(Long id) {
        return selfProvider.getObject().getCategories().stream().filter(category -> category.id().equals(id))
                           .findFirst()
                           .orElseThrow(() -> new IllegalArgumentException("Specialty category not found: " + id));
    }

    public SpecialtyDto.DetailResponse getDetail(Long id) {
        return selfProvider.getObject().getDetails().stream().filter(detail -> detail.id().equals(id)).findFirst()
                           .orElseThrow(() -> new IllegalArgumentException("Specialty detail not found: " + id));
    }
}
