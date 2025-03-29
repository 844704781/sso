package com.watermelon.sso.entity.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 分页请求基类
 */
@Data
public class PageRequest<T> {
    /**
     * 页码，从1开始
     */
    @NotNull(message = "Page number cannot be null")
    @Min(value = 1, message = "Page number must be at least 1")
    private Integer page;

    /**
     * 每页大小
     */
    @NotNull(message = "Page size cannot be null")
    @Min(value = 1, message = "Page size must be at least 1")
    private Integer size;

    /**
     * 查询条件
     */
    @Valid
    private T data;
} 