package com.watermelon.sso.web;

import com.watermelon.sso.common.Result;
import com.watermelon.sso.entity.common.PageRequest;
import com.watermelon.sso.entity.request.ConfigDtoRequest;
import com.watermelon.sso.entity.request.ConfigQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.SystemKeyValueResponse;
import com.watermelon.sso.service.SystemKeyValueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置管理接口
 */
@RestController
@RequestMapping("/api")
@Validated
public class ConfigWeb {

} 