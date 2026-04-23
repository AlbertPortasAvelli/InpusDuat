package com.inpusduat.inpusduat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seasons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;

    @Column(name = "season_number", nullable = false)
    private Integer seasonNumber;

    @Column(length = 255)
    private String title;

    @Column(name = "episode_count")
    private Integer episodeCount;

    @Column(name = "release_year")
    private Integer releaseYear;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Episode> episodes = new ArrayList<>();
}
