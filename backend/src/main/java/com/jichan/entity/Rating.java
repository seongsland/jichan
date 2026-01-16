package com.jichan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rating", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "expert_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rating extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "expert_id", nullable = false)
    private Long expertId;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Builder
    public Rating(Long userId, Long expertId, Integer score, String comment) {
        this.userId = userId;
        this.expertId = expertId;
        this.score = score;
        this.comment = comment;
    }

    public void updateRating(Integer score, String comment) {
        if (score != null) this.score = score;
        if (comment != null) this.comment = comment;
    }
}
