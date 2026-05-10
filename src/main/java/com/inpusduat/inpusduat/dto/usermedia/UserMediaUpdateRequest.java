package com.inpusduat.inpusduat.dto.usermedia;

import com.inpusduat.inpusduat.domain.UserMedia.Status;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UserMediaUpdateRequest {
    private Status status;

    @Min(1) @Max(10)
    private Integer rating;

    private String notes;
}