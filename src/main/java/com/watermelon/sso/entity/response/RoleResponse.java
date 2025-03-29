package com.watermelon.sso.entity.response;

import lombok.Data;

/**
 * 角色响应对象
 */
@Data
public class RoleResponse {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 角色名称
     */
    private String name;
    
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