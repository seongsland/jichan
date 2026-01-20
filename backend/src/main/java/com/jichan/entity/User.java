package com.jichan.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@ToString
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

    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible = false;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    private String phone;

    @Column(name = "phone_message", columnDefinition = "TEXT")
    private String phoneMessage;

    @Column(name = "average_rating", nullable = false)
    private int averageRating = 0;

    @Column(name = "review_count", nullable = false)
    private int reviewCount = 0;

    @Column(name = "min_hourly_rate", nullable = false)
    private int minHourlyRate = 0;


    @Builder
    public User(String name, String email, String password, String gender, String region,
                String introduction, Boolean isVisible, Boolean emailVerified, String phone, String phoneMessage,
                int averageRating, int reviewCount, int minHourlyRate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.region = region;
        this.introduction = introduction;
        this.isVisible = isVisible != null ? isVisible : false;
        this.emailVerified = emailVerified != null ? emailVerified : false;
        this.phone = phone;
        this.phoneMessage = phoneMessage;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.minHourlyRate = minHourlyRate;
    }

    public void updateProfile(String name, String gender, String region, String introduction,
                             Boolean isVisible, String phone, String phoneMessage) {
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

    public void updateRating(int averageRating, int reviewCount) {
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }

    public void updateMinHourlyRate(int minHourlyRate) {
        this.minHourlyRate = minHourlyRate;
    }

    public void increaseReviewCount() {
        this.reviewCount++;
    }
}
