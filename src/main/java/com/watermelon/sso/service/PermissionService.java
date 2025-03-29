package com.watermelon.sso.service;

import com.watermelon.sso.entity.request.PermissionQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.PermissionResponse;

/**
 * 权限管理Service
 */
public interface PermissionService {
    /**
     * 分页获取系统下的所有权限
     *
     * @param systemId 系统ID
     * @param page     页码
     * @param size     每页大小
     * @param query    查询条件
     * @return 权限列表(响应对象)
     */
    PageResponse<PermissionResponse> getPermissionPageBySystemId(Long systemId, int page, int size, PermissionQueryRequest query);
} 