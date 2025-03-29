package com.watermelon.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.watermelon.sso.entity.Permission;
import com.watermelon.sso.entity.common.PageResult;
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
        return buildPermissionPageResponse(result);
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
    private PageResponse<PermissionResponse> buildPermissionPageResponse(PageResult<Permission> pageResult) {
        if (pageResult == null) {
            return null;
        }
        
        List<PermissionResponse> responseList = toPermissionResponseList(pageResult.getResult());
        return new PageResponse<>(pageResult.getTotal(), responseList);
    }
} 