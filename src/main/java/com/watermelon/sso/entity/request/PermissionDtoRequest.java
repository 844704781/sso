package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 权限DTO请求
 */
@Data
public class PermissionDtoRequest {

    /**
     * 权限名称
     */
    @NotBlank(message = "Permission name cannot be empty")
    @Size(max = 32, message = "Permission name length cannot exceed 32 characters")
    @JsonProperty("name")
    private String name;

    /**
     * 权限URL
     */
    @NotBlank(message = "Permission URL cannot be empty")
    @Size(max = 256, message = "Permission URL length cannot exceed 256 characters")
    @JsonProperty("url")
    private String url;
} 