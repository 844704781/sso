package com.watermelon.sso.web;

import com.watermelon.sso.common.Result;
import com.watermelon.sso.entity.common.PageRequest;
import com.watermelon.sso.entity.request.RoleDtoRequest;
import com.watermelon.sso.entity.request.RolePermissionAssociationRequest;
import com.watermelon.sso.entity.request.RoleQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.PermissionResponse;
import com.watermelon.sso.entity.response.RolePermissionResponse;
import com.watermelon.sso.entity.response.RoleResponse;
import com.watermelon.sso.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理接口
 */
@RestController
@RequestMapping("/role")
@Validated
public class RoleWeb {

    @Autowired
    private RoleService roleService;

    /**
     * 分页获取系统所有角色列表
     */
    @GetMapping("/systems/{systemId}/roles")
    public Result<PageResponse<RoleResponse>> getRolesBySystemId(
            @PathVariable("systemId") Long systemId,
            @Valid PageRequest<RoleQueryRequest> request) {
        PageResponse<RoleResponse> response = roleService.getRolePageBySystemId(
                systemId,
                request.getPage(),
                request.getSize(),
                request.getData()
        );
        return Result.success(response);
    }

    /**
     * 创建角色
     */
    @PostMapping("/systems/{systemId}/roles")
    public Result<RoleResponse> createRole(
            @PathVariable("systemId") Long systemId,
            @RequestBody @Valid RoleDtoRequest request) {
        RoleResponse response = roleService.createRole(systemId, request);
        return Result.success(response);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/roles/{roleId}")
    public Result<Boolean> deleteRole(@PathVariable("roleId") Long roleId) {
        boolean success = roleService.deleteRole(roleId);
        return Result.success(success);
    }

    /**
     * 获取角色详情
     */
    @GetMapping("/roles/{roleId}")
    public Result<RoleResponse> getRoleDetail(@PathVariable("roleId") Long roleId) {
        RoleResponse response = roleService.getRoleById(roleId);
        return Result.success(response);
    }

    /**
     * 修改角色名称
     */
    @PutMapping("/roles/{roleId}")
    public Result<RoleResponse> updateRole(
            @PathVariable("roleId") Long roleId,
            @RequestBody @Valid RoleDtoRequest request) {
        RoleResponse response = roleService.updateRole(roleId, request);
        return Result.success(response);
    }

    /**
     * 获取角色对应的权限
     */
    @GetMapping("/roles/{roleId}/permissions")
    public Result<List<RolePermissionResponse>> getRolePermissions(@PathVariable("roleId") Long roleId) {
        List<RolePermissionResponse> response = roleService.getRolePermissions(roleId);
        return Result.success(response);
    }

    /**
     * 获取当前角色对应的系统的所有权限
     */
    @GetMapping("/roles/{roleId}/system-permissions")
    public Result<List<PermissionResponse>> getRoleSystemPermissions(@PathVariable("roleId") Long roleId) {
        List<PermissionResponse> response = roleService.getRoleSystemPermissions(roleId);
        return Result.success(response);
    }

    /**
     * 设置当前角色的权限
     */
    @PutMapping("/roles/{roleId}/permissions")
    public Result<Boolean> setRolePermissions(
            @PathVariable("roleId") Long roleId,
            @RequestBody @Valid RolePermissionAssociationRequest request) {
        boolean success = roleService.setRolePermissions(roleId, request);
        return Result.success(success);
    }
} 