package com.jichan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "rating", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "expert_id"})})
@Getter
@Builder
@AllArgsConstructor
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

    public void update(Integer score) {
        if (score != null) this.score = score;
    }
}
