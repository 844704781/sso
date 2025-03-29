package com.watermelon.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.watermelon.sso.common.ResultCode;
import com.watermelon.sso.common.ServiceException;
import com.watermelon.sso.entity.Permission;
import com.watermelon.sso.entity.Role;
import com.watermelon.sso.entity.RolePermissionAssociation;
import com.watermelon.sso.entity.common.PageResult;
import com.watermelon.sso.entity.request.RoleDtoRequest;
import com.watermelon.sso.entity.request.RolePermissionAssociationRequest;
import com.watermelon.sso.entity.request.RoleQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.PermissionResponse;
import com.watermelon.sso.entity.response.RolePermissionResponse;
import com.watermelon.sso.entity.response.RoleResponse;
import com.watermelon.sso.manager.PermissionManager;
import com.watermelon.sso.manager.RoleManager;
import com.watermelon.sso.manager.RolePermissionAssociationManager;
import com.watermelon.sso.service.RoleService;
import com.watermelon.sso.service.SysSystemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理Service实现
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private SysSystemService sysSystemService;

    @Autowired
    private PermissionManager permissionManager;

    @Autowired
    private RolePermissionAssociationManager rolePermissionAssociationManager;

    private static LambdaQueryWrapper<Role> setQueryWrapper(Long systemId, RoleQueryRequest query) {
        // 构建查询条件
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Role::getSystemId, systemId);

        // 添加查询条件
        if (query != null && StringUtils.hasText(query.getName())) {
            wrapper.like(Role::getName, query.getName());
        }
        return wrapper;
    }

    @Override
    public PageResponse<RoleResponse> getRolePageBySystemId(Long systemId, int page, int size, RoleQueryRequest query) {
        // 检查系统是否存在
        sysSystemService.getSystemById(systemId);

        LambdaQueryWrapper<Role> wrapper = setQueryWrapper(systemId, query);

        // 执行分页查询
        Page<Role> pageResult = roleManager.page(new Page<>(page, size), wrapper);

        // 转换为响应对象
        PageResult<Role> result = new PageResult<>(pageResult.getTotal(), pageResult.getRecords());
        return buildRolePageResponse(result, page, size);
    }

    @Override
    public List<RoleResponse> getAllRolesBySystemId(Long systemId) {
        // 检查系统是否存在
        sysSystemService.getSystemById(systemId);

        // 查询系统下所有角色
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Role::getSystemId, systemId);

        List<Role> roles = roleManager.list(wrapper);

        // 转换为响应对象
        return roles.stream()
                .map(this::buildRoleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse createRole(Long systemId, RoleDtoRequest request) {
        // 检查系统是否存在
        sysSystemService.getSystemById(systemId);

        // 检查角色名称是否已存在
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Role::getSystemId, systemId)
                .eq(Role::getName, request.getName());

        if (roleManager.count(wrapper) > 0) {
            throw new ServiceException(ResultCode.EXIST_ROLE_NAME, "Role name already exists");
        }

        // 创建角色
        Role role = new Role();
        role.setName(request.getName());
        role.setSystemId(systemId);

        boolean success = roleManager.save(role);
        if (!success) {
            throw new ServiceException(ResultCode.ROLE_CREATE_FAILED, "Failed to create role");
        }

        return buildRoleResponse(role);
    }

    @Override
    public boolean deleteRole(Long roleId) {
        // 获取角色
        Role role = getRoleEntityById(roleId);

        // 删除角色
        return roleManager.removeById(roleId);
    }

    @Override
    public RoleResponse getRoleById(Long roleId) {
        // 获取角色
        Role role = getRoleEntityById(roleId);

        // 转换为响应对象
        return buildRoleResponse(role);
    }

    @Override
    public RoleResponse updateRole(Long roleId, RoleDtoRequest request) {
        // 获取角色
        Role role = getRoleEntityById(roleId);

        // 检查角色名称是否已存在
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Role::getSystemId, role.getSystemId())
                .eq(Role::getName, request.getName())
                .ne(Role::getId, roleId);

        if (roleManager.count(wrapper) > 0) {
            throw new ServiceException(ResultCode.EXIST_ROLE_NAME, "Role name already exists");
        }

        // 更新角色
        role.setName(request.getName());

        boolean success = roleManager.updateById(role);
        if (!success) {
            throw new ServiceException(ResultCode.ROLE_UPDATE_FAILED, "Failed to update role");
        }

        return buildRoleResponse(role);
    }

    @Override
    public List<RolePermissionResponse> getRolePermissions(Long roleId) {
        // 获取角色
        Role role = getRoleEntityById(roleId);

        // 查询角色权限关联
        LambdaQueryWrapper<RolePermissionAssociation> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RolePermissionAssociation::getRoleId, roleId);

        List<RolePermissionAssociation> associations = rolePermissionAssociationManager.list(wrapper);

        if (associations.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取权限ID列表
        List<Long> permissionIds = associations.stream()
                .map(RolePermissionAssociation::getPermissionId)
                .collect(Collectors.toList());

        // 查询权限信息
        List<Permission> permissions = permissionManager.listByIds(permissionIds);

        // 转换为响应对象
        return associations.stream()
                .map(association -> {
                    RolePermissionResponse response = new RolePermissionResponse();
                    response.setId(association.getId());
                    response.setRoleId(association.getRoleId());
                    response.setPermissionId(association.getPermissionId());
                    response.setReadAll(association.getReadAll());

                    // 设置权限详情
                    permissions.stream()
                            .filter(p -> p.getId().equals(association.getPermissionId()))
                            .findFirst()
                            .ifPresent(permission -> response.setPermission(buildPermissionResponse(permission)));

                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionResponse> getRoleSystemPermissions(Long roleId) {
        // 获取角色
        Role role = getRoleEntityById(roleId);

        // 查询系统下所有权限
        LambdaQueryWrapper<Permission> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Permission::getSystemId, role.getSystemId());

        List<Permission> permissions = permissionManager.list(wrapper);

        // 转换为响应对象
        return permissions.stream()
                .map(this::buildPermissionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean setRolePermissions(Long roleId, RolePermissionAssociationRequest request) {
        // 获取角色
        Role role = getRoleEntityById(roleId);

        // 删除现有权限关联
        LambdaQueryWrapper<RolePermissionAssociation> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RolePermissionAssociation::getRoleId, roleId);
        rolePermissionAssociationManager.remove(wrapper);

        // 如果没有新权限，直接返回成功
        if (request.getPermissionIds().isEmpty()) {
            return true;
        }

        // 创建新权限关联
        List<RolePermissionAssociation> associations = request.getPermissionIds().stream()
                .map(permissionId -> {
                    RolePermissionAssociation association = new RolePermissionAssociation();
                    association.setRoleId(roleId);
                    association.setPermissionId(permissionId);
                    association.setReadAll(0); // 默认不允许读取所有数据
                    return association;
                })
                .collect(Collectors.toList());

        // 批量保存
        return rolePermissionAssociationManager.saveBatch(associations);
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

        // 时间戳转LocalDateTime
        if (entity.getCreateTime() != null) {
            response.setCreateTime(LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(entity.getCreateTime()),
                    ZoneId.systemDefault()));
        }
        if (entity.getUpdateTime() != null) {
            response.setUpdateTime(LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(entity.getUpdateTime()),
                    ZoneId.systemDefault()));
        }

        return response;
    }

    /**
     * 将Role实体列表转换为RoleResponse响应对象列表
     */
    private List<RoleResponse> toRoleResponseList(List<Role> entityList) {
        if (entityList == null) {
            return null;
        }

        return entityList.stream()
                .map(this::buildRoleResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将Role分页结果转换为RoleResponse分页响应
     */
    private PageResponse<RoleResponse> buildRolePageResponse(PageResult<Role> pageResult, Integer page, Integer size) {
        if (pageResult == null) {
            return null;
        }

        List<RoleResponse> responseList = toRoleResponseList(pageResult.getResult());
        return new PageResponse<>(responseList, pageResult.getTotal(), page, size);
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

        // 时间戳转LocalDateTime
        if (entity.getCreateTime() != null) {
            response.setCreateTime(LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(entity.getCreateTime()),
                    ZoneId.systemDefault()));
        }
        if (entity.getUpdateTime() != null) {
            response.setUpdateTime(LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(entity.getUpdateTime()),
                    ZoneId.systemDefault()));
        }

        return response;
    }

    /**
     * 根据ID获取角色实体
     */
    private Role getRoleEntityById(Long roleId) {
        Role role = roleManager.getById(roleId);
        if (role == null) {
            throw new ServiceException(ResultCode.NO_ITEM_ROLE, "Role does not exist");
        }
        return role;
    }
} 