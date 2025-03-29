package com.watermelon.sso.service;

import com.watermelon.sso.entity.request.RoleDtoRequest;
import com.watermelon.sso.entity.request.RolePermissionAssociationRequest;
import com.watermelon.sso.entity.request.RoleQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.PermissionResponse;
import com.watermelon.sso.entity.response.RolePermissionResponse;
import com.watermelon.sso.entity.response.RoleResponse;

import java.util.List;

/**
 * 角色管理服务接口
 */
public interface RoleService {

    /**
     * 获取系统下角色分页列表
     *
     * @param systemId 系统ID
     * @param page     页码
     * @param size     每页大小
     * @param request  查询条件
     * @return 分页结果
     */
    PageResponse<RoleResponse> getRolePageBySystemId(Long systemId, int page, int size, RoleQueryRequest request);

    /**
     * 获取系统下所有角色（不分页）
     *
     * @param systemId 系统ID
     * @return 角色列表
     */
    List<RoleResponse> getAllRolesBySystemId(Long systemId);

    /**
     * 创建角色
     *
     * @param systemId 系统ID
     * @param request  角色创建请求
     * @return 创建后的角色信息
     */
    RoleResponse createRole(Long systemId, RoleDtoRequest request);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return 是否删除成功
     */
    boolean deleteRole(Long roleId);

    /**
     * 获取角色详情
     *
     * @param roleId 角色ID
     * @return 角色详情
     */
    RoleResponse getRoleById(Long roleId);

    /**
     * 修改角色名称
     *
     * @param roleId  角色ID
     * @param request 角色修改请求
     * @return 修改后的角色信息
     */
    RoleResponse updateRole(Long roleId, RoleDtoRequest request);

    /**
     * 获取角色对应的权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<RolePermissionResponse> getRolePermissions(Long roleId);

    /**
     * 获取当前角色对应系统的所有权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<PermissionResponse> getRoleSystemPermissions(Long roleId);

    /**
     * 设置当前角色的权限
     *
     * @param roleId  角色ID
     * @param request 权限设置请求
     * @return 是否设置成功
     */
    boolean setRolePermissions(Long roleId, RolePermissionAssociationRequest request);
} 