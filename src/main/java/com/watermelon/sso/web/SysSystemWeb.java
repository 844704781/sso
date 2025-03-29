package com.watermelon.sso.web;

import com.watermelon.sso.common.Result;
import com.watermelon.sso.entity.common.PageRequest;
import com.watermelon.sso.entity.request.*;
import com.watermelon.sso.entity.response.*;
import com.watermelon.sso.service.PermissionService;
import com.watermelon.sso.service.RoleService;
import com.watermelon.sso.service.SysSystemService;
import com.watermelon.sso.service.SystemKeyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 系统管理接口
 */
@RestController
@RequestMapping("/systems")
@Validated
public class SysSystemWeb {

    @Autowired
    private SysSystemService sysSystemService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private SystemKeyValueService systemKeyValueService;

    @Autowired
    private RoleService roleService;

    /**
     * 分页获取系统列表
     */
    @GetMapping
    public Result<PageResponse<SysSystemResponse>> getSystemPage(@Valid PageRequest<SysSystemQueryRequest> request) {
        PageResponse<SysSystemResponse> response = sysSystemService.getSystemPage(
                request.getPage(),
                request.getSize(),
                request.getData()
        );
        return Result.success(response);
    }

    /**
     * 创建系统
     */
    @PostMapping
    public Result<SysSystemResponse> createSystem(@RequestBody @Valid SysSystemDtoRequest dto) {
        SysSystemResponse response = sysSystemService.createSystem(dto.getName());
        return Result.success(response);
    }

    /**
     * 修改系统名称
     */
    @PutMapping("/{systemId}")
    public Result<SysSystemResponse> updateSystem(
            @PathVariable("systemId") Long systemId,
            @RequestBody @Valid SysSystemDtoRequest dto) {
        SysSystemResponse response = sysSystemService.updateSystem(systemId, dto.getName());
        return Result.success(response);
    }

    /**
     * 删除系统
     */
    @DeleteMapping("/{systemId}")
    public Result<Boolean> deleteSystem(@PathVariable("systemId") Long systemId) {
        boolean success = sysSystemService.deleteSystem(systemId);
        return Result.success(success);
    }

    /**
     * 获取系统详情
     */
    @GetMapping("/{systemId}")
    public Result<SysSystemResponse> getSystemDetail(@PathVariable("systemId") Long systemId) {
        SysSystemResponse response = sysSystemService.getSystemById(systemId);
        return Result.success(response);
    }

    /**
     * 分页获取系统下的所有权限
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
     * 分页获取系统下的所有配置列表
     */
    @GetMapping("/systems/{systemId}/configs")
    public Result<PageResponse<SystemKeyValueResponse>> getConfigsBySystemId(
            @PathVariable("systemId") Long systemId,
            @Valid PageRequest<ConfigQueryRequest> request) {
        PageResponse<SystemKeyValueResponse> response = systemKeyValueService.getConfigPageBySystemId(
                systemId,
                request.getPage(),
                request.getSize(),
                request.getData()
        );
        return Result.success(response);
    }

    /**
     * 查看配置详情
     */
    @GetMapping("/configs/{configId}")
    public Result<SystemKeyValueResponse> getConfigDetail(
            @PathVariable("configId") Long configId) {
        SystemKeyValueResponse response = systemKeyValueService.getConfigById(configId);
        return Result.success(response);
    }

    /**
     * 创建配置
     */
    @PostMapping("/systems/{systemId}/configs")
    public Result<SystemKeyValueResponse> createConfig(
            @PathVariable("systemId") Long systemId,
            @RequestBody @Valid ConfigDtoRequest request) {
        SystemKeyValueResponse response = systemKeyValueService.createConfig(systemId, request);
        return Result.success(response);
    }

    /**
     * 修改配置
     */
    @PutMapping("/configs/{configId}")
    public Result<SystemKeyValueResponse> updateConfig(
            @PathVariable("configId") Long configId,
            @RequestBody @Valid ConfigDtoRequest request) {
        SystemKeyValueResponse response = systemKeyValueService.updateConfig(configId, request);
        return Result.success(response);
    }

    /**
     * 删除配置
     */
    @DeleteMapping("/configs/{configId}")
    public Result<Boolean> deleteConfig(
            @PathVariable("configId") Long configId) {
        boolean success = systemKeyValueService.deleteConfig(configId);
        return Result.success(success);
    }

    /**
     * 分页获取系统下的所有角色
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
} 