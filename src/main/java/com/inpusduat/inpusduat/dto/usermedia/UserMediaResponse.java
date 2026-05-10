package com.inpusduat.inpusduat.dto.usermedia;

import com.inpusduat.inpusduat.domain.UserMedia.Status;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@Builder
public class UserMediaResponse {
    private Long id;
    private Long mediaId;
    private String mediaTitle;
    private Status status;
    private Integer rating;
    private String notes;
    private LocalDateTime watchedAt;
    private LocalDateTime createdAt;
}