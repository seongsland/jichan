package com.jichan.controller;

import com.jichan.dto.ContactDto.ContactSliceResponse;
import com.jichan.dto.ContactDto.RatingRequest;
import com.jichan.dto.ContactDto.RatingResponse;
import com.jichan.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@Tag(name = "Contact", description = "지인 관련 API")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    @Operation(summary = "지인 목록 조회", description = "지인의 연락처 목록을 조회합니다. 카테고리 및 전문분야 필터링이 가능합니다.")
    public ResponseEntity<ContactSliceResponse> getContacts(Authentication authentication,
                                                            @RequestParam(required = false) Long category,
                                                            @RequestParam(required = false) Long specialty,
                                                            @RequestParam(defaultValue = "0") int page) {
        Long userId = Long.valueOf(authentication.getName());
        ContactSliceResponse response = contactService.getContacts(userId, category, specialty, page);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rating")
    @Operation(summary = "평점 등록/수정", description = "전문가에 대한 평점을 등록하거나 수정합니다.")
    public ResponseEntity<RatingResponse> createOrUpdateRating(Authentication authentication,
                                                               @Valid @RequestBody RatingRequest request) {
        Long userId = Long.valueOf(authentication.getName());
        RatingResponse response = contactService.createOrUpdateRating(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{expertId}")
    @Operation(summary = "연락처 삭제", description = "특정 전문가를 연락처 목록에서 삭제합니다.")
    public ResponseEntity<Void> deleteContact(Authentication authentication, @PathVariable Long expertId) {
        Long userId = Long.valueOf(authentication.getName());
        contactService.deleteContact(userId, expertId);
        return ResponseEntity.noContent().build();
    }
}
