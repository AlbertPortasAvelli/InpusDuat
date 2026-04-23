package com.inpusduat.inpusduat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_media",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "media_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status;

    @Column
    private Integer rating;

    @Column(name = "watched_at")
    private LocalDateTime watchedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    protected void onCreate() {
        if (status == null) status = Status.PENDING;
    }

    public enum Status {
        WATCHED, WATCHING, PENDING
    }
}