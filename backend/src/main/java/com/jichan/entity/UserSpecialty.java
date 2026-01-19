package com.jichan.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_specialty")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UserSpecialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "specialty_detail_id", nullable = false)
    private Long specialtyDetailId;

    @Column(name = "hourly_rate", nullable = false)
    private Integer hourlyRate;
}
