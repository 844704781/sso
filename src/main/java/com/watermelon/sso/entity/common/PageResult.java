package com.watermelon.sso.entity.common;

import lombok.Data;

import java.util.List;

/**
 * 分页结果
 */
@Data
public class PageResult<T> {
    /**
     * 总数
     */
    private long total;

    /**
     * 结果列表
     */
    private List<T> result;

    public PageResult(long total, List<T> result) {
        this.total = total;
        this.result = result;
    }
} 