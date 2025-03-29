package com.watermelon.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.watermelon.sso.entity.Role;
import com.watermelon.sso.entity.common.PageResult;
import com.watermelon.sso.entity.request.RoleQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.RoleResponse;
import com.watermelon.sso.manager.RoleManager;
import com.watermelon.sso.service.RoleService;
import com.watermelon.sso.service.SysSystemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    
    @Override
    public PageResponse<RoleResponse> getRolePageBySystemId(Long systemId, int page, int size, RoleQueryRequest query) {
        // 检查系统是否存在
        sysSystemService.getSystemById(systemId);
        
        // 构建查询条件
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Role::getSystemId, systemId);
        
        // 添加查询条件
        if (query != null && StringUtils.hasText(query.getName())) {
            wrapper.like(Role::getName, query.getName());
        }
        
        // 执行分页查询
        Page<Role> pageResult = roleManager.page(new Page<>(page, size), wrapper);
        
        // 转换为响应对象
        PageResult<Role> result = new PageResult<>(pageResult.getTotal(), pageResult.getRecords());
        return buildRolePageResponse(result);
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
    private PageResponse<RoleResponse> buildRolePageResponse(PageResult<Role> pageResult) {
        if (pageResult == null) {
            return null;
        }
        
        List<RoleResponse> responseList = toRoleResponseList(pageResult.getResult());
        return new PageResponse<>(pageResult.getTotal(), responseList);
    }
} 