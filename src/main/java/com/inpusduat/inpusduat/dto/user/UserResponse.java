package com.inpusduat.inpusduat.dto.user;

import com.inpusduat.inpusduat.domain.User;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private User.Role role;
    private Boolean active;
    private LocalDateTime createdAt;
}