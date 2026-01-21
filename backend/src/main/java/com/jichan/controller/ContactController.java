package com.jichan.controller;

import com.jichan.dto.ContactDto.ContactSliceResponse;
import com.jichan.dto.ContactDto.RatingRequest;
import com.jichan.dto.ContactDto.RatingResponse;
import com.jichan.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<ContactSliceResponse> getContacts(
            Authentication authentication,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Long specialty,
            @RequestParam(defaultValue = "0") int page) {
        Long userId = Long.valueOf(authentication.getName());
        ContactSliceResponse response = contactService.getContacts(userId, category, specialty, page);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rating")
    public ResponseEntity<RatingResponse> createOrUpdateRating(
            Authentication authentication,
            @Valid @RequestBody RatingRequest request) {
        Long userId = Long.valueOf(authentication.getName());
        RatingResponse response = contactService.createOrUpdateRating(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{expertId}")
    public ResponseEntity<Void> deleteContact(
            Authentication authentication,
            @PathVariable Long expertId) {
        Long userId = Long.valueOf(authentication.getName());
        contactService.deleteContact(userId, expertId);
        return ResponseEntity.noContent().build();
    }
}
