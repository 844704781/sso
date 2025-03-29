package com.watermelon.sso.util;

import com.watermelon.sso.entity.Permission;
import com.watermelon.sso.entity.Role;
import com.watermelon.sso.entity.SysSystem;
import com.watermelon.sso.entity.SystemKeyValue;
import com.watermelon.sso.entity.common.PageResult;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.PermissionResponse;
import com.watermelon.sso.entity.response.RoleResponse;
import com.watermelon.sso.entity.response.SysSystemResponse;
import com.watermelon.sso.entity.response.SystemKeyValueResponse;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Bean转换工具类
 */
public class BeanConverter {
    
    /**
     * 将SysSystem实体转换为SysSystemResponse响应对象
     */
    public static SysSystemResponse toSysSystemResponse(SysSystem entity) {
        if (entity == null) {
            return null;
        }
        
        SysSystemResponse response = new SysSystemResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
    
    /**
     * 将SysSystem实体列表转换为SysSystemResponse响应对象列表
     */
    public static List<SysSystemResponse> toSysSystemResponseList(List<SysSystem> entityList) {
        if (entityList == null) {
            return null;
        }
        
        return entityList.stream()
                .map(BeanConverter::toSysSystemResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 将SysSystem分页结果转换为SysSystemResponse分页响应
     */
    public static PageResponse<SysSystemResponse> toSysSystemPageResponse(PageResult<SysSystem> pageResult) {
        if (pageResult == null) {
            return null;
        }
        
        List<SysSystemResponse> responseList = toSysSystemResponseList(pageResult.getResult());
        return new PageResponse<>(pageResult.getTotal(), responseList);
    }
    
    /**
     * 将Role实体转换为RoleResponse响应对象
     */
    public static RoleResponse toRoleResponse(Role entity) {
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
    public static List<RoleResponse> toRoleResponseList(List<Role> entityList) {
        if (entityList == null) {
            return null;
        }
        
        return entityList.stream()
                .map(BeanConverter::toRoleResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 将Role分页结果转换为RoleResponse分页响应
     */
    public static PageResponse<RoleResponse> toRolePageResponse(PageResult<Role> pageResult) {
        if (pageResult == null) {
            return null;
        }
        
        List<RoleResponse> responseList = toRoleResponseList(pageResult.getResult());
        return new PageResponse<>(pageResult.getTotal(), responseList);
    }
    
    /**
     * 将Permission实体转换为PermissionResponse响应对象
     */
    public static PermissionResponse toPermissionResponse(Permission entity) {
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
    public static List<PermissionResponse> toPermissionResponseList(List<Permission> entityList) {
        if (entityList == null) {
            return null;
        }
        
        return entityList.stream()
                .map(BeanConverter::toPermissionResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 将Permission分页结果转换为PermissionResponse分页响应
     */
    public static PageResponse<PermissionResponse> toPermissionPageResponse(PageResult<Permission> pageResult) {
        if (pageResult == null) {
            return null;
        }
        
        List<PermissionResponse> responseList = toPermissionResponseList(pageResult.getResult());
        return new PageResponse<>(pageResult.getTotal(), responseList);
    }
    
    /**
     * 将SystemKeyValue实体转换为SystemKeyValueResponse响应对象
     */
    public static SystemKeyValueResponse toSystemKeyValueResponse(SystemKeyValue entity) {
        if (entity == null) {
            return null;
        }
        
        SystemKeyValueResponse response = new SystemKeyValueResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
    
    /**
     * 将SystemKeyValue实体列表转换为SystemKeyValueResponse响应对象列表
     */
    public static List<SystemKeyValueResponse> toSystemKeyValueResponseList(List<SystemKeyValue> entityList) {
        if (entityList == null) {
            return null;
        }
        
        return entityList.stream()
                .map(BeanConverter::toSystemKeyValueResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 将SystemKeyValue分页结果转换为SystemKeyValueResponse分页响应
     */
    public static PageResponse<SystemKeyValueResponse> toSystemKeyValuePageResponse(PageResult<SystemKeyValue> pageResult) {
        if (pageResult == null) {
            return null;
        }
        
        List<SystemKeyValueResponse> responseList = toSystemKeyValueResponseList(pageResult.getResult());
        return new PageResponse<>(pageResult.getTotal(), responseList);
    }
} 