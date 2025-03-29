package com.watermelon.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watermelon.sso.common.ResultCode;
import com.watermelon.sso.common.ServiceException;
import com.watermelon.sso.entity.SysSystem;
import com.watermelon.sso.entity.SystemKeyValue;
import com.watermelon.sso.entity.common.PageResult;
import com.watermelon.sso.entity.request.ConfigDtoRequest;
import com.watermelon.sso.entity.request.ConfigQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.SystemKeyValueResponse;
import com.watermelon.sso.mapper.SysSystemMapper;
import com.watermelon.sso.mapper.SystemKeyValueMapper;
import com.watermelon.sso.service.SystemKeyValueService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统配置服务实现类
 */
@Service
public class SystemKeyValueServiceImpl extends ServiceImpl<SystemKeyValueMapper, SystemKeyValue> implements SystemKeyValueService {

    @Autowired
    private SysSystemMapper sysSystemMapper;

    @Override
    public PageResponse<SystemKeyValueResponse> getConfigPageBySystemId(Long systemId, Integer page, Integer size, ConfigQueryRequest request) {
        // 查询系统是否存在
        SysSystem system = sysSystemMapper.selectById(systemId);
        if (system == null) {
            throw new ServiceException(ResultCode.NO_ITEM_SYS_SYSTEM, "系统不存在");
        }

        // 构建查询条件
        LambdaQueryWrapper<SystemKeyValue> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SystemKeyValue::getSystemId, systemId);

        // 添加查询条件
        if (request != null) {
            if (StringUtils.isNotBlank(request.getKey())) {
                queryWrapper.like(SystemKeyValue::getKey, request.getKey());
            }
            if (StringUtils.isNotBlank(request.getRemark())) {
                queryWrapper.like(SystemKeyValue::getRemark, request.getRemark());
            }
        }

        // 按创建时间倒序排序
        queryWrapper.orderByDesc(SystemKeyValue::getCreateTime);

        // 分页查询
        Page<SystemKeyValue> pageResult = this.page(new Page<>(page, size), queryWrapper);

        // 转换结果
        List<SystemKeyValue> records = pageResult.getRecords();
        PageResult<SystemKeyValue> result = new PageResult<>(pageResult.getTotal(), records);
        return buildSystemKeyValuePageResponse(result, page, size);
    }

    @Override
    public SystemKeyValueResponse getConfigById(Long configId) {
        SystemKeyValue config = this.getById(configId);
        if (config == null) {
            throw new ServiceException(ResultCode.NO_ITEM_SYSTEM_KEY_VALUE, "配置不存在");
        }

        return buildSystemKeyValueResponse(config);
    }

    @Override
    public SystemKeyValueResponse createConfig(Long systemId, ConfigDtoRequest request) {
        // 查询系统是否存在
        SysSystem system = sysSystemMapper.selectById(systemId);
        if (system == null) {
            throw new ServiceException(ResultCode.NO_ITEM_SYS_SYSTEM, "系统不存在");
        }

        // 检查键名是否已存在
        LambdaQueryWrapper<SystemKeyValue> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SystemKeyValue::getSystemId, systemId)
                .eq(SystemKeyValue::getKey, request.getKey());
        if (this.count(queryWrapper) > 0) {
            throw new ServiceException(ResultCode.EXIST_SYSTEM_KEY_VALUE, "配置键已存在");
        }

        // 创建配置
        SystemKeyValue config = new SystemKeyValue();
        config.setSystemId(systemId);
        config.setKey(request.getKey());
        config.setValue(request.getValue());
        config.setName(request.getName());
        config.setRemark(request.getRemark());

        this.save(config);

        // 返回结果
        return buildSystemKeyValueResponse(config);
    }

    @Override
    public SystemKeyValueResponse updateConfig(Long configId, ConfigDtoRequest request) {
        // 查询配置是否存在
        SystemKeyValue config = this.getById(configId);
        if (config == null) {
            throw new ServiceException(ResultCode.NO_ITEM_SYSTEM_KEY_VALUE, "配置不存在");
        }

        // 检查键名是否与其他配置冲突
        if (!config.getKey().equals(request.getKey())) {
            LambdaQueryWrapper<SystemKeyValue> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(SystemKeyValue::getSystemId, config.getSystemId())
                    .eq(SystemKeyValue::getKey, request.getKey())
                    .ne(SystemKeyValue::getId, configId);
            if (this.count(queryWrapper) > 0) {
                throw new ServiceException(ResultCode.EXIST_SYSTEM_KEY_VALUE, "配置键已存在");
            }
        }

        // 更新配置
        config.setKey(request.getKey());
        config.setValue(request.getValue());
        config.setName(request.getName());
        config.setRemark(request.getRemark());

        this.updateById(config);

        // 返回结果
        return buildSystemKeyValueResponse(config);
    }

    @Override
    public boolean deleteConfig(Long configId) {
        // 查询配置是否存在
        SystemKeyValue config = this.getById(configId);
        if (config == null) {
            throw new ServiceException(ResultCode.NO_ITEM_SYSTEM_KEY_VALUE, "配置不存在");
        }

        return this.removeById(configId);
    }

    /**
     * 将SystemKeyValue实体转换为SystemKeyValueResponse响应对象
     */
    private SystemKeyValueResponse buildSystemKeyValueResponse(SystemKeyValue entity) {
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
    private List<SystemKeyValueResponse> toSystemKeyValueResponseList(List<SystemKeyValue> entityList) {
        if (entityList == null) {
            return null;
        }

        return entityList.stream()
                .map(this::buildSystemKeyValueResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将SystemKeyValue分页结果转换为SystemKeyValueResponse分页响应
     */
    private PageResponse<SystemKeyValueResponse> buildSystemKeyValuePageResponse(PageResult<SystemKeyValue> pageResult, Integer page, Integer size) {
        if (pageResult == null) {
            return null;
        }

        List<SystemKeyValueResponse> responseList = toSystemKeyValueResponseList(pageResult.getResult());
        return new PageResponse<>(responseList, pageResult.getTotal(), page, size);
    }
} 