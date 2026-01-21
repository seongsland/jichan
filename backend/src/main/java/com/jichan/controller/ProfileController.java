package com.jichan.controller;

import com.jichan.dto.ProfileDto.ContactViewRequest;
import com.jichan.dto.ProfileDto.ContactViewResponse;
import com.jichan.dto.ProfileDto.ProfileListResponse;
import com.jichan.entity.ContactType;
import com.jichan.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileListResponse> getProfiles(
            Authentication authentication,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Long specialty,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int page) {
        Long viewerId = Long.valueOf(authentication.getName());
        ProfileListResponse response = profileService.getProfiles(viewerId, category, specialty, sortBy, page);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/contact_view")
    public ResponseEntity<ContactViewResponse> viewContact(
            Authentication authentication,
            @Valid @RequestBody ContactViewRequest request) {
        Long viewerId = Long.valueOf(authentication.getName());
        ContactType contactType = ContactType.valueOf(request.contactType());
        ContactViewResponse response = profileService.viewContact(viewerId, request.expertId(), contactType);
        return ResponseEntity.ok(response);
    }
}
