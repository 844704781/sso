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

    private long page;

    private long size;

    /**
     * 结果列表
     */
    private List<T> result;

    public PageResponse(List<T> result, long total, long page, long size) {
        this.result = result;
        this.total = total;
        this.page = page;
        this.size = size;
    }
} 