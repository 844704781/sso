package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 角色权限关联请求
 */
@Data
public class RolePermissionAssociationRequest {
    
    /**
     * 角色ID（批量操作时使用）
     */
    @JsonProperty("role_id")
    private Long roleId;
    
    /**
     * 权限ID列表
     */
    @NotEmpty(message = "权限ID列表不能为空")
    @JsonProperty("permission_ids")
    private List<Long> permissionIds;
} 