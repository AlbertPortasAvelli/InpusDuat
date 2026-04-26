package com.inpusduat.inpusduat.dto.media;

import com.inpusduat.inpusduat.domain.Media;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MediaRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 255)
    private String originalTitle;

    @NotNull(message = "Type is required")
    private Media.MediaType type;

    @Size(max = 100)
    private String genre;

    @Size(max = 50)
    private String language;

    @Min(value = 1888, message = "Release year must be after 1888")
    @Max(value = 2100, message = "Release year must be before 2100")
    private Integer releaseYear;

    private String synopsis;

    @Size(max = 500)
    private String posterUrl;
}