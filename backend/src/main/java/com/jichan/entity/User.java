package com.jichan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String gender;

    private String region;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Builder.Default
    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible = false;

    @Builder.Default
    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @Pattern(regexp = "^$|^\\d{3}-\\d{4}-\\d{4}$", message = "핸드폰 번호 형식이 올바르지 않습니다.")
    private String phone;

    @Column(name = "phone_message", columnDefinition = "TEXT")
    private String phoneMessage;

    @Builder.Default
    @Column(name = "average_rating", nullable = false)
    private int averageRating = 0; // Changed to double

    @Builder.Default
    @Column(name = "review_count", nullable = false)
    private int reviewCount = 0;

    @Builder.Default
    @Column(name = "min_hourly_rate", nullable = false)
    private int minHourlyRate = 0;


    public void updateProfile(String name, String gender, String region, String introduction, Boolean isVisible,
                              String phone, String phoneMessage) {
        if (name != null) this.name = name;
        if (gender != null) this.gender = gender;
        if (region != null) this.region = region;
        if (introduction != null) this.introduction = introduction;
        if (isVisible != null) this.isVisible = isVisible;
        if (phone != null) this.phone = phone;
        if (phoneMessage != null) this.phoneMessage = phoneMessage;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public void updateRating(int averageRating) {
        this.averageRating = averageRating;
    }

    public void updateMinHourlyRate(int minHourlyRate) {
        this.minHourlyRate = minHourlyRate;
    }

    public void updateReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
