package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 按系统查询权限请求
 */
@Data
public class PermissionQueryBySystemRequest {

    /**
     * 系统ID
     */
    @NotNull(message = "System ID cannot be empty")
    @JsonProperty("system_id")
    private Long systemId;
} 