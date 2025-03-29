package com.watermelon.sso.web;

import com.watermelon.sso.common.Result;
import com.watermelon.sso.entity.common.PageRequest;
import com.watermelon.sso.entity.request.*;
import com.watermelon.sso.entity.response.*;
import com.watermelon.sso.service.PermissionService;
import com.watermelon.sso.service.RoleService;
import com.watermelon.sso.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理接口
 */
@RestController
@RequestMapping("/api")
@Validated
public class UserWeb {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取用户列表
     */
    @GetMapping("/users")
    public Result<PageResponse<UserResponse>> getUserList(@Valid PageRequest<UserQueryRequest> request) {
        PageResponse<UserResponse> response = userService.getUserPage(
                request.getPage(),
                request.getSize(),
                request.getData()
        );
        return Result.success(response);
    }

    /**
     * 创建用户
     */
    @PostMapping("/users")
    public Result<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        UserResponse response = userService.createUser(request);
        return Result.success(response);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/users/{uuid}")
    public Result<UserResponse> getUserDetail(@PathVariable("uuid") String uuid) {
        UserResponse response = userService.getUserDetail(uuid);
        return Result.success(response);
    }

    /**
     * 暂停/开启用户
     */
    @PutMapping("/users/{uuid}/status")
    public Result<Boolean> updateUserStatus(
            @PathVariable("uuid") String uuid,
            @RequestBody @Valid UserStatusRequest request) {
        boolean success = userService.updateUserStatus(uuid, request);
        return Result.success(success);
    }

    /**
     * 生成用户API Token
     */
    @PostMapping("/users/{uuid}/token")
    public Result<TokenResponse> generateUserToken(
            @PathVariable("uuid") String uuid,
            @RequestBody @Valid UserTokenRequest request) {
        TokenResponse response = userService.generateUserToken(uuid, request);
        return Result.success(response);
    }

    /**
     * 管理员修改用户密码
     */
    @PutMapping("/users/{uuid}/password")
    public Result<Boolean> updateUserPassword(
            @PathVariable("uuid") String uuid,
            @RequestBody @Valid UserPasswordRequest request) {
        boolean success = userService.updateUserPassword(uuid, request);
        return Result.success(success);
    }

    /**
     * 获取用户角色
     */
    @GetMapping("/users/{uuid}/roles")
    public Result<List<RoleResponse>> getUserRoles(@PathVariable("uuid") String uuid) {
        List<RoleResponse> response = userService.getUserRoles(uuid);
        return Result.success(response);
    }

    /**
     * 设置用户角色
     */
    @PutMapping("/users/{uuid}/roles")
    public Result<Boolean> setUserRoles(
            @PathVariable("uuid") String uuid,
            @RequestBody @Valid UserRoleAssociationRequest request) {
        boolean success = userService.setUserRoles(uuid, request);
        return Result.success(success);
    }

    /**
     * 获取用户权限
     */
    @GetMapping("/users/{uuid}/permissions")
    public Result<List<PermissionResponse>> getUserPermissions(
            @PathVariable("uuid") String uuid,
            @Valid PermissionQueryBySystemRequest request) {
        List<PermissionResponse> response = userService.getUserPermissions(uuid, request.getSystemId());
        return Result.success(response);
    }

    /**
     * 获取系统所有角色(无需分页)
     */
    @GetMapping("/systems/{systemId}/all-roles")
    public Result<List<RoleResponse>> getAllRoles(@PathVariable("systemId") Long systemId) {
        List<RoleResponse> response = roleService.getAllRolesBySystemId(systemId);
        return Result.success(response);
    }

    /**
     * 获取系统所有权限
     */
    @GetMapping("/systems/{systemId}/all-permissions")
    public Result<List<PermissionResponse>> getAllPermissions(@PathVariable("systemId") Long systemId) {
        List<PermissionResponse> response = permissionService.getAllPermissionsBySystemId(systemId);
        return Result.success(response);
    }
} 