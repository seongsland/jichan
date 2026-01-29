package com.jichan.controller;

import com.jichan.dto.ProfileDto.ContactViewRequest;
import com.jichan.dto.ProfileDto.ContactViewResponse;
import com.jichan.dto.ProfileDto.ProfileListResponse;
import com.jichan.dto.ProfileDto.ProfileRequest;
import com.jichan.entity.ContactType;
import com.jichan.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "전문가 검색 관련 API")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    @Operation(summary = "전문가 프로필 목록 조회", description = "조건에 맞는 전문가 프로필 목록을 조회합니다.")
    public ResponseEntity<ProfileListResponse> getProfiles(Authentication authentication,
                                                           ProfileRequest profilesRequest) {
        Long viewerId = (authentication != null) ? Long.valueOf(authentication.getName()) : null;
        ProfileListResponse response = profileService.getProfiles(viewerId, profilesRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/contact_view")
    @Operation(summary = "전문가 연락처 보기", description = "전문가의 연락처 정보를 조회하고 조회 기록을 남깁니다.")
    public ResponseEntity<ContactViewResponse> viewContact(Authentication authentication,
                                                           @Valid @RequestBody ContactViewRequest request) {
        Long viewerId = Long.valueOf(authentication.getName());
        ContactType contactType = ContactType.valueOf(request.contactType());
        ContactViewResponse response = profileService.viewContact(viewerId, request.expertId(), contactType);
        return ResponseEntity.ok(response);
    }

}
