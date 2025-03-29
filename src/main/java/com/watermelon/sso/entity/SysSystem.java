package com.watermelon.sso.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统表
 */
@Data
@TableName("sys_system")
@Accessors(chain = true)
public class SysSystem {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 系统名称
     */
    private String name;

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