package com.watermelon.sso.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统配置响应
 */
@Data
public class SystemKeyValueResponse {

    /**
     * 配置ID
     */
    @JsonProperty("id")
    private Long id;

    /**
     * 系统ID
     */
    @JsonProperty("system_id")
    private Long systemId;

    /**
     * 键名
     */
    @JsonProperty("key")
    private String key;

    /**
     * 键值
     */
    @JsonProperty("value")
    private String value;

    /**
     * 配置名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 备注
     */
    @JsonProperty("remark")
    private String remark;

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