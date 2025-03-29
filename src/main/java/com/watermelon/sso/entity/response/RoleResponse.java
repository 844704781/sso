package com.watermelon.sso.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色响应
 */
@Data
public class RoleResponse {

    /**
     * ID
     */
    @JsonProperty("id")
    private Long id;

    /**
     * 角色名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 系统ID
     */
    @JsonProperty("system_id")
    private Long systemId;

    /**
     * 创建时间
     */
    @JsonProperty("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonProperty("update_time")
    private LocalDateTime updateTime;
} 