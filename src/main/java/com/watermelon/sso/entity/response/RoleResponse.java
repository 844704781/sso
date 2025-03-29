package com.watermelon.sso.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
    private Long createTime;

    /**
     * 更新时间
     */
    @JsonProperty("update_time")
    private Long updateTime;
} 