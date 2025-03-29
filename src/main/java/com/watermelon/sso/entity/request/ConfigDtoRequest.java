package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 配置DTO
 */
@Data
public class ConfigDtoRequest {
    /**
     * 配置键
     */
    @NotBlank(message = "Config key cannot be empty")
    @Size(max = 32, message = "Config key length cannot exceed 32 characters")
    @JsonProperty("key")
    private String key;
    
    /**
     * 配置值
     */
    @NotBlank(message = "Config value cannot be empty")
    @Size(max = 4096, message = "Config value length cannot exceed 4096 characters")
    @JsonProperty("value")
    private String value;
    
    /**
     * 配置名称
     */
    @NotBlank(message = "Config name cannot be empty")
    @Size(max = 64, message = "Config name length cannot exceed 64 characters")
    @JsonProperty("name")
    private String name;
    
    /**
     * 备注
     */
    @Size(max = 1024, message = "Remark length cannot exceed 1024 characters")
    @JsonProperty("remark")
    private String remark;
} 