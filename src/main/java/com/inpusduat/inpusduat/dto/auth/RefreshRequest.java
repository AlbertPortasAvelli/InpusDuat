// src/main/java/com/inpusduat/inpusduat/dto/auth/RefreshRequest.java
package com.inpusduat.inpusduat.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshRequest {
    @NotBlank
    private String refreshToken;
}