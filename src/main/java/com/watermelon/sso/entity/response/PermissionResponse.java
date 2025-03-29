package com.watermelon.sso.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * 权限响应
 */
@Data
public class PermissionResponse {

    /**
     * ID
     */
    @JsonProperty("id")
    private Long id;

    /**
     * 权限名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * URL地址
     */
    @JsonProperty("url")
    private String url;

    /**
     * 系统ID
     */
    @JsonProperty("system_id")
    private Long systemId;

    /**
     * 是否读取所有数据（0-否，1-是）
     */
    @JsonProperty("read_all")
    private Integer readAll;

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