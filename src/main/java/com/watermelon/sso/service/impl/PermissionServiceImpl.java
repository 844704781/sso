package com.watermelon.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.watermelon.sso.common.ResultCode;
import com.watermelon.sso.common.ServiceException;
import com.watermelon.sso.entity.Permission;
import com.watermelon.sso.entity.common.PageResult;
import com.watermelon.sso.entity.request.PermissionDtoRequest;
import com.watermelon.sso.entity.request.PermissionQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.PermissionResponse;
import com.watermelon.sso.manager.PermissionManager;
import com.watermelon.sso.service.PermissionService;
import com.watermelon.sso.service.SysSystemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限管理Service实现
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionManager permissionManager;

    @Autowired
    private SysSystemService sysSystemService;

    @Override
    public PageResponse<PermissionResponse> getPermissionPageBySystemId(Long systemId, int page, int size, PermissionQueryRequest query) {
        // 检查系统是否存在
        sysSystemService.getSystemById(systemId);

        // 构建查询条件
        LambdaQueryWrapper<Permission> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Permission::getSystemId, systemId);

        // 添加查询条件
        if (query != null) {
            if (StringUtils.hasText(query.getName())) {
                wrapper.like(Permission::getName, query.getName());
            }
            if (StringUtils.hasText(query.getUrl())) {
                wrapper.like(Permission::getUrl, query.getUrl());
            }
        }

        // 执行分页查询
        Page<Permission> pageResult = permissionManager.page(new Page<>(page, size), wrapper);

        // 转换为响应对象
        PageResult<Permission> result = new PageResult<>(pageResult.getTotal(), pageResult.getRecords());
        return buildPermissionPageResponse(result, page, size);
    }

    @Override
    public List<PermissionResponse> getAllPermissionsBySystemId(Long systemId) {
        // 检查系统是否存在
        sysSystemService.getSystemById(systemId);

        // 查询系统下所有权限
        LambdaQueryWrapper<Permission> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Permission::getSystemId, systemId);

        List<Permission> permissions = permissionManager.list(wrapper);

        // 转换为响应对象
        return permissions.stream()
                .map(this::buildPermissionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PermissionResponse createPermission(Long systemId, PermissionDtoRequest request) {
        // 检查系统是否存在
        sysSystemService.getSystemById(systemId);

        // 检查URL是否已存在
        LambdaQueryWrapper<Permission> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Permission::getSystemId, systemId)
                .eq(Permission::getUrl, request.getUrl());

        if (permissionManager.count(wrapper) > 0) {
            throw new ServiceException(ResultCode.EXIST_PERMISSION_URL, "Permission URL already exists");
        }

        // 创建权限
        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setUrl(request.getUrl());
        permission.setSystemId(systemId);

        boolean success = permissionManager.save(permission);
        if (!success) {
            throw new ServiceException(ResultCode.PERMISSION_CREATE_FAILED, "Failed to create permission");
        }

        return buildPermissionResponse(permission);
    }

    @Override
    public boolean deletePermission(Long permissionId) {
        // 获取权限
        Permission permission = getPermissionEntityById(permissionId);

        // 删除权限
        return permissionManager.removeById(permissionId);
    }

    @Override
    public PermissionResponse getPermissionById(Long permissionId) {
        // 获取权限
        Permission permission = getPermissionEntityById(permissionId);

        // 转换为响应对象
        return buildPermissionResponse(permission);
    }

    @Override
    public PermissionResponse updatePermission(Long permissionId, PermissionDtoRequest request) {
        // 获取权限
        Permission permission = getPermissionEntityById(permissionId);

        // 检查URL是否已存在
        LambdaQueryWrapper<Permission> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Permission::getSystemId, permission.getSystemId())
                .eq(Permission::getUrl, request.getUrl())
                .ne(Permission::getId, permissionId);

        if (permissionManager.count(wrapper) > 0) {
            throw new ServiceException(ResultCode.EXIST_PERMISSION_URL, "Permission URL already exists");
        }

        // 更新权限
        permission.setName(request.getName());
        permission.setUrl(request.getUrl());

        boolean success = permissionManager.updateById(permission);
        if (!success) {
            throw new ServiceException(ResultCode.PERMISSION_UPDATE_FAILED, "Failed to update permission");
        }

        return buildPermissionResponse(permission);
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
     * 将Permission实体列表转换为PermissionResponse响应对象列表
     */
    private List<PermissionResponse> toPermissionResponseList(List<Permission> entityList) {
        if (entityList == null) {
            return null;
        }

        return entityList.stream()
                .map(this::buildPermissionResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将Permission分页结果转换为PermissionResponse分页响应
     */
    private PageResponse<PermissionResponse> buildPermissionPageResponse(PageResult<Permission> pageResult, long page, long size) {
        if (pageResult == null) {
            return null;
        }

        List<PermissionResponse> responseList = toPermissionResponseList(pageResult.getResult());
        return new PageResponse<>(responseList, pageResult.getTotal(), page, size);
    }

    /**
     * 根据ID获取权限实体
     */
    private Permission getPermissionEntityById(Long permissionId) {
        Permission permission = permissionManager.getById(permissionId);
        if (permission == null) {
            throw new ServiceException(ResultCode.NO_ITEM_PERMISSION, "Permission does not exist");
        }
        return permission;
    }
} 