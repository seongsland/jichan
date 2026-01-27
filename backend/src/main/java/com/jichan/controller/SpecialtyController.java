package com.jichan.controller;

import com.jichan.dto.SpecialtyDto;
import com.jichan.service.SpecialtyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/specialty")
@RequiredArgsConstructor
@Tag(name = "Specialty", description = "전문 분야 관련 API")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @GetMapping("/categories")
    @Operation(summary = "카테고리 목록 조회", description = "모든 전문 분야 카테고리 목록을 조회합니다.")
    public ResponseEntity<List<SpecialtyDto.CategoryResponse>> getCategories() {
        List<SpecialtyDto.CategoryResponse> categories = specialtyService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/details")
    @Operation(summary = "세부 분야 목록 조회", description = "모든 세부 전문 분야 목록을 조회합니다.")
    public ResponseEntity<List<SpecialtyDto.DetailResponse>> getDetails() {
        List<SpecialtyDto.DetailResponse> details = specialtyService.getDetails();
        return ResponseEntity.ok(details);
    }
}
