package com.watermelon.sso.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统用户表
 */
@Data
@TableName("user")
@Accessors(chain = true)
public class User {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 系统用户编号
     */
    private String uuid;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * token
     */
    private String token;

    /**
     * 是否暂停使用,0:否,1:是
     */
    private Integer suspend;

    /**
     * 是否是超级管理员
     */
    private Integer admin;

    /**
     * 邮箱
     */
    private String email;

    /**
     * token过期时间
     */
    private Long expiration;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 用户电话国家码
     */
    private String phoneCountryCode;

    /**
     * 用户允许访问的服务
     */
    private String allowService;

    /**
     * 用户上一次访问时间
     */
    private Long lastOnlineTime;

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