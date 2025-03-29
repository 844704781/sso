package com.watermelon.sso.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 角色权限关联响应
 */
@Data
public class RolePermissionResponse {

    /**
     * ID
     */
    @JsonProperty("id")
    private Long id;

    /**
     * 权限ID
     */
    @JsonProperty("permission_id")
    private Long permissionId;

    /**
     * 角色ID
     */
    @JsonProperty("role_id")
    private Long roleId;

    /**
     * 是否读取所有数据（0-否，1-是）
     */
    @JsonProperty("read_all")
    private Integer readAll;

    /**
     * 权限详情
     */
    @JsonProperty("permission")
    private PermissionResponse permission;
} 