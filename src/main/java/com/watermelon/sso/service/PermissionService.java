package com.watermelon.sso.service;

import com.watermelon.sso.entity.request.PermissionDtoRequest;
import com.watermelon.sso.entity.request.PermissionQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.PermissionResponse;

import java.util.List;

/**
 * 权限管理服务接口
 */
public interface PermissionService {

    /**
     * 获取系统下权限分页列表
     *
     * @param systemId 系统ID
     * @param page     页码
     * @param size     每页大小
     * @param request  查询条件
     * @return 分页结果
     */
    PageResponse<PermissionResponse> getPermissionPageBySystemId(Long systemId, int page, int size, PermissionQueryRequest request);

    /**
     * 获取系统下所有权限（不分页）
     *
     * @param systemId 系统ID
     * @return 权限列表
     */
    List<PermissionResponse> getAllPermissionsBySystemId(Long systemId);

    /**
     * 创建权限
     *
     * @param systemId 系统ID
     * @param request  权限创建请求
     * @return 创建后的权限信息
     */
    PermissionResponse createPermission(Long systemId, PermissionDtoRequest request);

    /**
     * 删除权限
     *
     * @param permissionId 权限ID
     * @return 是否删除成功
     */
    boolean deletePermission(Long permissionId);

    /**
     * 获取权限详情
     *
     * @param permissionId 权限ID
     * @return 权限详情
     */
    PermissionResponse getPermissionById(Long permissionId);

    /**
     * 修改权限信息
     *
     * @param permissionId 权限ID
     * @param request      权限修改请求
     * @return 修改后的权限信息
     */
    PermissionResponse updatePermission(Long permissionId, PermissionDtoRequest request);
} 