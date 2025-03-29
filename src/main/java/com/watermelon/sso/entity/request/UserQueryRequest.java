package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 用户查询请求
 */
@Data
public class UserQueryRequest {

    /**
     * 用户编号
     */
    @JsonProperty("uuid")
    private String uuid;

    /**
     * 用户昵称
     */
    @JsonProperty("nick_name")
    private String nickName;

    /**
     * 手机号
     */
    @JsonProperty("phone")
    private String phone;

    /**
     * 邮箱地址
     */
    @JsonProperty("email")
    private String email;

    /**
     * 是否暂停（0-正常，1-暂停）
     */
    @JsonProperty("suspend")
    private Integer suspend;
} 