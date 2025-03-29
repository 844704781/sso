package com.watermelon.sso.service;

import com.watermelon.sso.entity.request.*;
import com.watermelon.sso.entity.response.*;

import java.util.List;

/**
 * 用户管理服务接口
 */
public interface UserService {

    /**
     * 获取用户分页列表
     *
     * @param page    页码
     * @param size    每页大小
     * @param request 查询条件
     * @return 分页结果
     */
    PageResponse<UserResponse> getUserPage(Integer page, Integer size, UserQueryRequest request);

    /**
     * 创建用户
     *
     * @param request 用户信息
     * @return 创建的用户信息
     */
    UserResponse createUser(UserCreateRequest request);

    /**
     * 修改用户状态
     *
     * @param uuid    用户UUID
     * @param request 状态信息
     * @return 是否成功
     */
    boolean updateUserStatus(String uuid, UserStatusRequest request);

    /**
     * 获取用户详情
     *
     * @param uuid 用户UUID
     * @return 用户详情
     */
    UserResponse getUserDetail(String uuid);

    /**
     * 生成用户API Token
     *
     * @param uuid    用户UUID
     * @param request 令牌过期时间
     * @return Token信息
     */
    ApiTokenResponse generateApiToken(String uuid, UserTokenRequest request);

    /**
     * 管理员修改用户密码
     *
     * @param uuid    用户UUID
     * @param request 密码信息
     * @return 是否成功
     */
    boolean updateUserPassword(String uuid, UserPasswordRequest request);

    /**
     * 获取用户的角色列表
     *
     * @param uuid 用户UUID
     * @return 角色列表
     */
    List<RoleResponse> getUserRoles(String uuid);

    /**
     * 设置用户角色
     *
     * @param uuid    用户UUID
     * @param request 角色ID列表
     * @return 是否成功
     */
    boolean setUserRoles(String uuid, UserRoleAssociationRequest request);

    /**
     * 获取用户权限
     *
     * @param uuid     用户UUID
     * @param systemId 系统ID
     * @return 权限列表
     */
    List<PermissionResponse> getUserPermissions(String uuid, Long systemId);
} 