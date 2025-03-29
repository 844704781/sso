package com.watermelon.sso.entity.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 系统DTO
 */
@Data
public class SysSystemDtoRequest {
    /**
     * 系统名称
     */
    @NotBlank(message = "System name cannot be empty")
    @Size(max = 32, message = "System name length cannot exceed 32 characters")
    private String name;
} 