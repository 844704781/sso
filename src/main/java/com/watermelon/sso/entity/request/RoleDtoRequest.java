package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 角色DTO请求
 */
@Data
public class RoleDtoRequest {

    /**
     * 角色名称
     */
    @NotBlank(message = "Role name cannot be empty")
    @Size(max = 32, message = "Role name length cannot exceed 32 characters")
    @JsonProperty("name")
    private String name;
} 