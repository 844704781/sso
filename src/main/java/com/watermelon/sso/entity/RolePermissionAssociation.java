package com.watermelon.sso.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色权限关联表
 */
@Data
@TableName("role_permission_association")
@Accessors(chain = true)
public class RolePermissionAssociation {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色编号
     */
    private Long roleId;

    /**
     * 权限编号
     */
    private Long permissionId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    /**
     * 是否允许读取当前权限的所有内容
     */
    private Integer readAll;
} 