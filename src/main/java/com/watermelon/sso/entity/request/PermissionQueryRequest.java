package com.watermelon.sso.entity.request;

import lombok.Data;

/**
 * 权限查询条件
 */
@Data
public class PermissionQueryRequest {
    /**
     * 权限名称关键字
     */
    private String name;
    
    /**
     * URL关键字
     */
    private String url;
} 