package com.watermelon.sso.service;

import com.watermelon.sso.entity.request.SysSystemQueryRequest;
import com.watermelon.sso.entity.response.PageResponse;
import com.watermelon.sso.entity.response.SysSystemResponse;

/**
 * 系统管理Service
 */
public interface SysSystemService {
    /**
     * 分页获取系统列表
     *
     * @param page  页码
     * @param size  每页大小
     * @param query 查询条件
     * @return 系统列表(响应对象)
     */
    PageResponse<SysSystemResponse> getSystemPage(int page, int size, SysSystemQueryRequest query);

    /**
     * 创建系统
     *
     * @param name 系统名称
     * @return 创建的系统(响应对象)
     */
    SysSystemResponse createSystem(String name);

    /**
     * 修改系统名称
     *
     * @param id   系统ID
     * @param name 新系统名称
     * @return 修改后的系统(响应对象)
     */
    SysSystemResponse updateSystem(Long id, String name);

    /**
     * 删除系统
     *
     * @param id 系统ID
     * @return 是否删除成功
     */
    boolean deleteSystem(Long id);

    /**
     * 获取系统详情
     *
     * @param id 系统ID
     * @return 系统详情(响应对象)
     */
    SysSystemResponse getSystemById(Long id);
} 