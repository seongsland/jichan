package com.jichan.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_log", uniqueConstraints = {@UniqueConstraint(columnNames = {"viewer_id", "expert_id", "contact_type"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContactLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "viewer_id", nullable = false)
    private Long viewerId;

    @Column(name = "expert_id", nullable = false)
    private Long expertId;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type", nullable = false)
    private ContactType contactType;

    @Column(name = "viewed_at", nullable = false)
    private LocalDateTime viewedAt;

    @Builder
    public ContactLog(Long viewerId, Long expertId, ContactType contactType) {
        this.viewerId = viewerId;
        this.expertId = expertId;
        this.contactType = contactType;
        this.viewedAt = LocalDateTime.now();
    }
}
