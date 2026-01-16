package com.jichan.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "specialty_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecialtyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private SpecialtyCategory category;

    @Column(nullable = false)
    private String name;


    @Builder
    public SpecialtyDetail(SpecialtyCategory category, String name) {
        this.category = category;
        this.name = name;
    }
}
