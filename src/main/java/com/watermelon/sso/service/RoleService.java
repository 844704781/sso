package com.watermelon.sso.service;

import com.watermelon.sso.entity.request.RoleQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.RoleResponse;

/**
 * 角色管理Service
 */
public interface RoleService {
    /**
     * 分页获取系统下的所有角色
     *
     * @param systemId 系统ID
     * @param page     页码
     * @param size     每页大小
     * @param query    查询条件
     * @return 角色列表(响应对象)
     */
    PageResponse<RoleResponse> getRolePageBySystemId(Long systemId, int page, int size, RoleQueryRequest query);
} 