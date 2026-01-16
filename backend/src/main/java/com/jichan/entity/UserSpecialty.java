package com.jichan.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_specialty")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSpecialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_detail_id", nullable = false)
    private SpecialtyDetail specialtyDetail;

    @Column(name = "hourly_rate", nullable = false)
    private Integer hourlyRate;

    @Builder
    public UserSpecialty(Long userId, SpecialtyDetail specialtyDetail, Integer hourlyRate) {
        this.userId = userId;
        this.specialtyDetail = specialtyDetail;
        this.hourlyRate = hourlyRate;
    }

    public void updateHourlyRate(Integer hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
