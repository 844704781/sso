package com.watermelon.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.watermelon.sso.common.ResultCode;
import com.watermelon.sso.common.ServiceException;
import com.watermelon.sso.entity.SysSystem;
import com.watermelon.sso.entity.common.PageResult;
import com.watermelon.sso.entity.request.SysSystemQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.SysSystemResponse;
import com.watermelon.sso.manager.SysSystemManager;
import com.watermelon.sso.service.SysSystemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统管理Service实现
 */
@Service
public class SysSystemServiceImpl implements SysSystemService {

    @Autowired
    private SysSystemManager sysSystemManager;

    @Override
    public PageResponse<SysSystemResponse> getSystemPage(int page, int size, SysSystemQueryRequest request) {
        // 添加查询条件
        QueryWrapper<SysSystem> wrapper = setQueryWrapper(request);

        // 执行分页查询
        Page<SysSystem> pageResult = sysSystemManager.page(new Page<>(page, size), wrapper);

        // 转换为响应对象
        PageResult<SysSystem> result = new PageResult<>(pageResult.getTotal(), pageResult.getRecords());
        return buildSysSystemPageResponse(result);
    }

    private QueryWrapper<SysSystem> setQueryWrapper(SysSystemQueryRequest request) {
        QueryWrapper<SysSystem> wrapper = new QueryWrapper<>();
        if (request != null && StringUtils.hasText(request.getName())) {
            wrapper.lambda().like(SysSystem::getName, request.getName());
        }
        return wrapper;
    }

    /**
     * 将SysSystem实体转换为SysSystemResponse响应对象
     */
    private SysSystemResponse buildSysSystemResponse(SysSystem entity) {
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
    private List<SysSystemResponse> toSysSystemResponseList(List<SysSystem> sysSystems) {
        if (sysSystems == null) {
            return null;
        }

        return sysSystems.stream()
                .map(this::buildSysSystemResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将SysSystem分页结果转换为SysSystemResponse分页响应
     */
    private PageResponse<SysSystemResponse> buildSysSystemPageResponse(PageResult<SysSystem> pageResult) {
        if (pageResult == null) {
            return null;
        }

        List<SysSystemResponse> responseList = toSysSystemResponseList(pageResult.getResult());
        return new PageResponse<>(pageResult.getTotal(), responseList);
    }

    @Override
    public SysSystemResponse createSystem(String name) {
        // 检查是否已存在同名系统
        LambdaQueryWrapper<SysSystem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysSystem::getName, name);

        if (sysSystemManager.count(wrapper) > 0) {
            throw new ServiceException(ResultCode.INVALID_ARG, "系统名称已存在");
        }

        // 创建新系统
        SysSystem system = new SysSystem();
        system.setName(name);

        boolean success = sysSystemManager.save(system);
        if (!success) {
            throw new ServiceException(ResultCode.INVALID_ARG, "创建系统失败");
        }

        // 转换为响应对象
        return buildSysSystemResponse(system);
    }

    @Override
    public SysSystemResponse updateSystem(Long id, String name) {
        // 检查系统是否存在
        SysSystem system = getSysSystemById(id);

        // 检查新名称是否与其他系统重复
        LambdaQueryWrapper<SysSystem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysSystem::getName, name)
                .ne(SysSystem::getId, id);

        if (sysSystemManager.count(wrapper) > 0) {
            throw new ServiceException(ResultCode.INVALID_ARG, "系统名称已存在");
        }

        // 更新系统名称
        system.setName(name);
        boolean success = sysSystemManager.updateById(system);

        if (!success) {
            throw new ServiceException(ResultCode.INVALID_ARG, "更新系统失败");
        }

        // 转换为响应对象
        return buildSysSystemResponse(system);
    }

    @Override
    public boolean deleteSystem(Long id) {
        // 检查系统是否存在
        getSysSystemById(id);

        // 删除系统
        return sysSystemManager.removeById(id);
    }

    @Override
    public SysSystemResponse getSystemById(Long id) {
        // 转换为响应对象
        return buildSysSystemResponse(getSysSystemById(id));
    }

    /**
     * 获取系统实体
     *
     * @param id 系统ID
     * @return 系统实体
     */
    private SysSystem getSysSystemById(Long id) {
        SysSystem system = sysSystemManager.getById(id);

        if (Objects.isNull(system)) {
            throw new ServiceException(ResultCode.INVALID_ARG, "系统不存在");
        }

        return system;
    }
} 