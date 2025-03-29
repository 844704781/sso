package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 配置查询请求
 */
@Data
public class ConfigQueryRequest {
    
    /**
     * 键名
     */
    @JsonProperty("key")
    private String key;
    
    /**
     * 备注
     */
    @JsonProperty("remark")
    private String remark;
} 