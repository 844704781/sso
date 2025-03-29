package com.watermelon.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.watermelon.sso.common.ResultCode;
import com.watermelon.sso.common.ServiceException;
import com.watermelon.sso.entity.*;
import com.watermelon.sso.entity.common.PageResult;
import com.watermelon.sso.entity.request.*;
import com.watermelon.sso.entity.response.*;
import com.watermelon.sso.manager.*;
import com.watermelon.sso.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户管理Service实现
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserManager userManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private UserRoleAssociationManager userRoleAssociationManager;

    @Autowired
    private PermissionManager permissionManager;

    @Autowired
    private RolePermissionAssociationManager rolePermissionAssociationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static LambdaQueryWrapper<User> setQueryWrapper(UserQueryRequest request) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();

        // 添加查询条件
        if (request != null) {
            if (StringUtils.hasText(request.getUuid())) {
                wrapper.like(User::getUuid, request.getUuid());
            }
            if (StringUtils.hasText(request.getNickName())) {
                wrapper.like(User::getNickName, request.getNickName());
            }
            if (StringUtils.hasText(request.getPhone())) {
                wrapper.like(User::getPhone, request.getPhone());
            }
            if (StringUtils.hasText(request.getEmail())) {
                wrapper.like(User::getEmail, request.getEmail());
            }
            if (request.getSuspend() != null) {
                wrapper.eq(User::getSuspend, request.getSuspend());
            }
        }
        return wrapper;
    }

    @Override
    public PageResponse<UserResponse> getUserPage(Integer page, Integer size, UserQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<User> wrapper = setQueryWrapper(request);

        // 执行分页查询
        Page<User> pageResult = userManager.page(new Page<>(page, size), wrapper);

        // 转换为响应对象
        PageResult<User> result = new PageResult<>(pageResult.getTotal(), pageResult.getRecords());
        return buildUserPageResponse(result, page, size);
    }

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        // 检查UUID是否已存在
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUuid, request.getUuid());
        if (userManager.count(wrapper) > 0) {
            throw new ServiceException(ResultCode.EXIST_USER_UUID, "User UUID already exists");
        }

        // 检查手机号是否已存在
        if (StringUtils.hasText(request.getPhone())) {
            wrapper = Wrappers.lambdaQuery();
            wrapper.eq(User::getPhone, request.getPhone());
            if (userManager.count(wrapper) > 0) {
                throw new ServiceException(ResultCode.EXIST_USER_PHONE, "Phone number already exists");
            }
        }

        // 检查邮箱是否已存在
        if (StringUtils.hasText(request.getEmail())) {
            wrapper = Wrappers.lambdaQuery();
            wrapper.eq(User::getEmail, request.getEmail());
            if (userManager.count(wrapper) > 0) {
                throw new ServiceException(ResultCode.EXIST_USER_EMAIL, "Email already exists");
            }
        }

        // 创建用户
        User user = new User();
        user.setUuid(request.getUuid());
        user.setNickName(request.getNickName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAvatarUrl(request.getAvatarUrl());
        user.setPhoneCountryCode(request.getPhoneCountryCode());
        user.setAllowService(request.getAllowService());
        user.setSuspend(0); // 默认不暂停
        user.setAdmin(request.getAdmin() != null ? request.getAdmin() : 0); // 默认非管理员

        // 保存用户
        boolean success = userManager.save(user);
        if (!success) {
            throw new ServiceException(ResultCode.USER_CREATE_FAILED, "Failed to create user");
        }

        // 返回用户信息
        return buildUserResponse(user);
    }

    @Override
    public boolean updateUserStatus(String uuid, UserStatusRequest request) {
        // 查询用户
        User user = getUserByUuid(uuid);

        // 更新用户状态
        user.setSuspend(request.getSuspend());

        // 保存更新
        return userManager.updateById(user);
    }

    @Override
    public UserResponse getUserDetail(String uuid) {
        // 查询用户
        User user = getUserByUuid(uuid);

        // 转换为响应对象
        return buildUserResponse(user);
    }

    @Override
    public ApiTokenResponse generateApiToken(String uuid, UserTokenRequest request) {
        // 查询用户
        User user = getUserByUuid(uuid);

        String apiToken = RandomStringUtils.randomAlphanumeric(256);

        // 设置内部token和过期时间
        user.setApiToken(apiToken);
        user.setExpiration(request.getExpiration());

        // 保存更新
        boolean success = userManager.updateById(user);
        if (!success) {
            throw new ServiceException(ResultCode.USER_TOKEN_FAILED, "Failed to generate user token");
        }

        // 构建响应
        ApiTokenResponse response = new ApiTokenResponse();
        response.setApiToken(apiToken);
        response.setExpiration(request.getExpiration());
        return response;
    }

    @Override
    public boolean updateUserPassword(String uuid, UserPasswordRequest request) {
        // 查询用户
        User user = getUserByUuid(uuid);

        // 更新密码
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 保存更新
        return userManager.updateById(user);
    }

    @Override
    public List<RoleResponse> getUserRoles(String uuid) {
        // 查询用户
        User user = getUserByUuid(uuid);

        // 查询用户角色关联
        LambdaQueryWrapper<UserRoleAssociation> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRoleAssociation::getUserId, user.getId());
        List<UserRoleAssociation> associations = userRoleAssociationManager.list(wrapper);

        if (associations.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取角色ID列表
        List<Long> roleIds = associations.stream()
                .map(UserRoleAssociation::getRoleId)
                .collect(Collectors.toList());

        // 查询角色信息
        List<Role> roles = roleManager.listByIds(roleIds);

        // 转换为响应对象
        return roles.stream()
                .map(this::buildRoleResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean setUserRoles(String uuid, UserRoleAssociationRequest request) {
        // 查询用户
        User user = getUserByUuid(uuid);

        // 删除现有角色关联
        LambdaQueryWrapper<UserRoleAssociation> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRoleAssociation::getUserId, user.getId());
        userRoleAssociationManager.remove(wrapper);

        // 如果没有新角色，直接返回成功
        if (request.getRoleIds().isEmpty()) {
            return true;
        }

        // 创建新角色关联
        List<UserRoleAssociation> associations = request.getRoleIds().stream()
                .map(roleId -> {
                    UserRoleAssociation association = new UserRoleAssociation();
                    association.setUserId(user.getId());
                    association.setRoleId(roleId);
                    return association;
                })
                .collect(Collectors.toList());

        // 批量保存
        return userRoleAssociationManager.saveBatch(associations);
    }

    @Override
    public List<PermissionResponse> getUserPermissions(String uuid, Long systemId) {
        // 查询用户
        User user = getUserByUuid(uuid);

        // 如果是管理员，返回系统所有权限
        if (user.getAdmin() != null && user.getAdmin() == 1) {
            LambdaQueryWrapper<Permission> permWrapper = Wrappers.lambdaQuery();
            permWrapper.eq(Permission::getSystemId, systemId);
            List<Permission> permissions = permissionManager.list(permWrapper);

            return permissions.stream()
                    .map(this::buildPermissionResponse)
                    .collect(Collectors.toList());
        }

        // 查询用户角色关联
        LambdaQueryWrapper<UserRoleAssociation> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRoleAssociation::getUserId, user.getId());
        List<UserRoleAssociation> userRoleAssociations = userRoleAssociationManager.list(wrapper);

        if (userRoleAssociations.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取角色ID列表
        List<Long> roleIds = userRoleAssociations.stream()
                .map(UserRoleAssociation::getRoleId)
                .collect(Collectors.toList());

        // 查询角色信息，过滤指定系统的角色
        LambdaQueryWrapper<Role> roleWrapper = Wrappers.lambdaQuery();
        roleWrapper.eq(Role::getSystemId, systemId).in(Role::getId, roleIds);
        List<Role> systemRoles = roleManager.list(roleWrapper);

        if (systemRoles.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取系统角色ID列表
        List<Long> systemRoleIds = systemRoles.stream()
                .map(Role::getId)
                .collect(Collectors.toList());

        // 查询角色权限关联
        LambdaQueryWrapper<RolePermissionAssociation> rpWrapper = Wrappers.lambdaQuery();
        rpWrapper.in(RolePermissionAssociation::getRoleId, systemRoleIds);
        List<RolePermissionAssociation> rolePermAssociations = rolePermissionAssociationManager.list(rpWrapper);

        if (rolePermAssociations.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取权限ID列表，去重
        Set<Long> permissionIds = rolePermAssociations.stream()
                .map(RolePermissionAssociation::getPermissionId)
                .collect(Collectors.toSet());

        // 查询权限信息
        List<Permission> permissions = permissionManager.listByIds(permissionIds);

        // 转换为响应对象
        return permissions.stream()
                .map(this::buildPermissionResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将User实体转换为UserResponse响应对象
     */
    private UserResponse buildUserResponse(User entity) {
        if (entity == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(entity, response);

        return response;
    }

    /**
     * 将User实体列表转换为UserResponse响应对象列表
     */
    private List<UserResponse> toUserResponseList(List<User> entityList) {
        if (entityList == null) {
            return null;
        }

        return entityList.stream()
                .map(this::buildUserResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将User分页结果转换为UserResponse分页响应
     */
    private PageResponse<UserResponse> buildUserPageResponse(PageResult<User> pageResult, Integer page, Integer size) {
        if (pageResult == null) {
            return null;
        }

        List<UserResponse> responseList = toUserResponseList(pageResult.getResult());
        return new PageResponse<>(responseList, pageResult.getTotal(), page, size);
    }

    /**
     * 将Role实体转换为RoleResponse响应对象
     */
    private RoleResponse buildRoleResponse(Role entity) {
        if (entity == null) {
            return null;
        }

        RoleResponse response = new RoleResponse();
        BeanUtils.copyProperties(entity, response);

        return response;
    }

    /**
     * 将Permission实体转换为PermissionResponse响应对象
     */
    private PermissionResponse buildPermissionResponse(Permission entity) {
        if (entity == null) {
            return null;
        }

        PermissionResponse response = new PermissionResponse();
        BeanUtils.copyProperties(entity, response);

        return response;
    }

    /**
     * 根据UUID获取用户
     */
    private User getUserByUuid(String uuid) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUuid, uuid);
        User user = userManager.getOne(wrapper);

        if (user == null) {
            throw new ServiceException(ResultCode.NO_ITEM_USER, "User does not exist");
        }

        return user;
    }
} 