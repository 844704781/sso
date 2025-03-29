package com.watermelon.sso.entity.response;

import lombok.Data;

import java.util.List;

/**
 * 分页响应结果
 */
@Data
public class PageResponse<T> {
    /**
     * 总数
     */
    private long total;

    /**
     * 结果列表
     */
    private List<T> result;

    public PageResponse(long total, List<T> result) {
        this.total = total;
        this.result = result;
    }
} 