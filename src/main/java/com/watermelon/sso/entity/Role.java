package com.watermelon.sso.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色表
 */
@Data
@TableName("role")
@Accessors(chain = true)
public class Role {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Long isDeleted;
} 