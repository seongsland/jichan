package com.jichan.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "specialty_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecialtyCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    @Builder
    public SpecialtyCategory(String name) {
        this.name = name;
    }
}
