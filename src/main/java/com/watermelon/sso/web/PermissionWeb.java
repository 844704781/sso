package com.watermelon.sso.web;

import com.watermelon.sso.common.Result;
import com.watermelon.sso.entity.common.PageRequest;
import com.watermelon.sso.entity.request.PermissionDtoRequest;
import com.watermelon.sso.entity.request.PermissionQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.PermissionResponse;
import com.watermelon.sso.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 权限管理接口
 */
@RestController
@RequestMapping("/api")
@Validated
public class PermissionWeb {

    @Autowired
    private PermissionService permissionService;

    /**
     * 分页获取系统权限列表
     */
    @GetMapping("/systems/{systemId}/permissions")
    public Result<PageResponse<PermissionResponse>> getPermissionsBySystemId(
            @PathVariable("systemId") Long systemId,
            @Valid PageRequest<PermissionQueryRequest> request) {
        PageResponse<PermissionResponse> response = permissionService.getPermissionPageBySystemId(
                systemId,
                request.getPage(),
                request.getSize(),
                request.getData()
        );
        return Result.success(response);
    }

    /**
     * 创建权限
     */
    @PostMapping("/systems/{systemId}/permissions")
    public Result<PermissionResponse> createPermission(
            @PathVariable("systemId") Long systemId,
            @RequestBody @Valid PermissionDtoRequest request) {
        PermissionResponse response = permissionService.createPermission(systemId, request);
        return Result.success(response);
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/permissions/{permissionId}")
    public Result<Boolean> deletePermission(@PathVariable("permissionId") Long permissionId) {
        boolean success = permissionService.deletePermission(permissionId);
        return Result.success(success);
    }

    /**
     * 获取权限详情
     */
    @GetMapping("/permissions/{permissionId}")
    public Result<PermissionResponse> getPermissionDetail(@PathVariable("permissionId") Long permissionId) {
        PermissionResponse response = permissionService.getPermissionById(permissionId);
        return Result.success(response);
    }

    /**
     * 修改权限信息
     */
    @PutMapping("/permissions/{permissionId}")
    public Result<PermissionResponse> updatePermission(
            @PathVariable("permissionId") Long permissionId,
            @RequestBody @Valid PermissionDtoRequest request) {
        PermissionResponse response = permissionService.updatePermission(permissionId, request);
        return Result.success(response);
    }
} 