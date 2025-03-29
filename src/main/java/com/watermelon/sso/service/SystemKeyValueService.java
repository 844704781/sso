package com.watermelon.sso.service;

import com.watermelon.sso.entity.request.ConfigDtoRequest;
import com.watermelon.sso.entity.request.ConfigQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.SystemKeyValueResponse;

/**
 * 系统配置服务接口
 */
public interface SystemKeyValueService {

    /**
     * 分页获取系统配置列表
     *
     * @param systemId 系统ID
     * @param page     页码
     * @param size     每页大小
     * @param request  查询条件
     * @return 分页结果
     */
    PageResponse<SystemKeyValueResponse> getConfigPageBySystemId(Long systemId, Integer page, Integer size, ConfigQueryRequest request);

    /**
     * 根据ID获取配置详情
     *
     * @param configId 配置ID
     * @return 配置详情
     */
    SystemKeyValueResponse getConfigById(Long configId);

    /**
     * 创建配置
     *
     * @param systemId 系统ID
     * @param request  配置信息
     * @return 创建后的配置
     */
    SystemKeyValueResponse createConfig(Long systemId, ConfigDtoRequest request);

    /**
     * 更新配置
     *
     * @param configId 配置ID
     * @param request  配置信息
     * @return 更新后的配置
     */
    SystemKeyValueResponse updateConfig(Long configId, ConfigDtoRequest request);

    /**
     * 删除配置
     *
     * @param configId 配置ID
     * @return 是否删除成功
     */
    boolean deleteConfig(Long configId);
} 