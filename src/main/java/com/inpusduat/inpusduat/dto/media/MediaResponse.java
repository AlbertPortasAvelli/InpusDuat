package com.inpusduat.inpusduat.dto.media;

import com.inpusduat.inpusduat.domain.Media;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class MediaResponse {
    private Long id;
    private String title;
    private String originalTitle;
    private Media.MediaType type;
    private String genre;
    private String language;
    private Integer releaseYear;
    private String synopsis;
    private String posterUrl;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}