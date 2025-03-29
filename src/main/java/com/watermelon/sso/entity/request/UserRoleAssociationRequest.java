package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 用户角色关联请求
 */
@Data
public class UserRoleAssociationRequest {
    
    /**
     * 用户ID（批量操作时使用）
     */
    @JsonProperty("user_id")
    private Long userId;
    
    /**
     * 角色ID列表
     */
    @NotEmpty(message = "角色ID列表不能为空")
    @JsonProperty("role_ids")
    private List<Long> roleIds;
} 