package com.watermelon.sso.common;

public class ResultCode {
    public static final int OK = 0;
    public static final int INVALID_ARG = 10000;
    public static final int NO_ITEM_SYS_SYSTEM = 11000;
    public static final int NO_ITEM_SYSTEM_KEY_VALUE = 12000;
    public static final int EXIST_SYSTEM_KEY_VALUE = 12001;

    // User相关错误码
    public static final int NO_ITEM_USER = 13000;        // 用户不存在
    public static final int EXIST_USER_UUID = 13001;     // 用户UUID已存在
    public static final int EXIST_USER_PHONE = 13002;    // 用户手机号已存在
    public static final int EXIST_USER_EMAIL = 13003;    // 用户邮箱已存在
    public static final int USER_CREATE_FAILED = 13004;  // 创建用户失败
    public static final int USER_TOKEN_FAILED = 13005;   // 生成用户Token失败

    // Role相关错误码
    public static final int NO_ITEM_ROLE = 14000;        // 角色不存在
    public static final int EXIST_ROLE_NAME = 14001;     // 角色名称已存在
    public static final int ROLE_CREATE_FAILED = 14002;  // 创建角色失败
    public static final int ROLE_UPDATE_FAILED = 14003;  // 更新角色失败

    // Permission相关错误码
    public static final int NO_ITEM_PERMISSION = 15000;   // 权限不存在
    public static final int EXIST_PERMISSION_URL = 15001; // 权限URL已存在
    public static final int PERMISSION_CREATE_FAILED = 15002; // 创建权限失败
    public static final int PERMISSION_UPDATE_FAILED = 15003; // 更新权限失败

    // 认证相关错误码
    public static final int EMAIL_SEND_LIMIT_EXCEEDED = 16000; // 邮箱验证码发送频率超出限制
    public static final int EMAIL_CODE_INVALID = 16001;       // 邮箱验证码无效
    public static final int EMAIL_CODE_EXPIRED = 16002;       // 邮箱验证码已过期
}
