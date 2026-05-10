// src/main/java/com/inpusduat/inpusduat/dto/media/MediaSearchResponse.java
package com.inpusduat.inpusduat.dto.media;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaSearchResponse {
    private Long id;
    private String title;
    private String originalTitle;
    private String type;
    private String genre;
    private String language;
    private Integer releaseYear;
    private String synopsis;
    private String posterUrl;
}