package com.watermelon.sso.entity.response;

import lombok.Data;

/**
 * 权限响应对象
 */
@Data
public class PermissionResponse {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 权限名称
     */
    private String name;
    
    /**
     * 权限URL
     */
    private String url;
    
    /**
     * 系统编号
     */
    private Long systemId;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
} 