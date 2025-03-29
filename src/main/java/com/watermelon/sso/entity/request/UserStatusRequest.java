package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户状态请求
 */
@Data
public class UserStatusRequest {

    /**
     * 是否暂停（0-正常，1-暂停）
     */
    @NotNull(message = "Suspend status cannot be empty")
    @JsonProperty("suspend")
    private Integer suspend;
} 