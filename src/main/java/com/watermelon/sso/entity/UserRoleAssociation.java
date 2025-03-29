package com.watermelon.sso.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户角色关联表
 */
@Data
@TableName("user_role_association")
@Accessors(chain = true)
public class UserRoleAssociation {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 角色编号
     */
    private Long roleId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;
} 