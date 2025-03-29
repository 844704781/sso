package com.watermelon.sso.entity.request;

import lombok.Data;

/**
 * 角色查询条件
 */
@Data
public class RoleQueryRequest {
    /**
     * 角色名称关键字
     */
    private String name;
} 