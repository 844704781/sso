package com.watermelon.sso.web;

import com.watermelon.sso.common.Result;
import com.watermelon.sso.entity.common.PageRequest;
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


} 