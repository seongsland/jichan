package com.jichan.controller;

import com.jichan.dto.ContactDto.ContactListResponse;
import com.jichan.dto.ContactDto.RatingRequest;
import com.jichan.dto.ContactDto.RatingResponse;
import com.jichan.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<List<ContactListResponse>> getContacts(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<ContactListResponse> response = contactService.getContacts(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rating")
    public ResponseEntity<RatingResponse> createRating(
            Authentication authentication,
            @Valid @RequestBody RatingRequest request) {
        Long userId = getUserIdFromAuthentication(authentication);
        RatingResponse response = contactService.createRating(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return contactService.getUserIdByEmail(email);
    }
}
