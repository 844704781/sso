package com.watermelon.sso.entity.response;

import lombok.Data;

/**
 * 系统响应对象
 */
@Data
public class SysSystemResponse {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 系统名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
} 