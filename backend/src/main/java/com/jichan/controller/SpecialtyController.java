package com.jichan.controller;

import com.jichan.dto.SpecialtyDto;
import com.jichan.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/specialty")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @GetMapping("/categories")
    public ResponseEntity<List<SpecialtyDto.CategoryResponse>> getCategories() {
        List<SpecialtyDto.CategoryResponse> categories = specialtyService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/details")
    public ResponseEntity<List<SpecialtyDto.DetailResponse>> getDetails() {
        List<SpecialtyDto.DetailResponse> details = specialtyService.getDetails();
        return ResponseEntity.ok(details);
    }
}
