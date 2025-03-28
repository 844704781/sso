-- SSO管理系统初始化SQL
-- MySQL 8.0

-- 系统表
CREATE TABLE IF NOT EXISTS `sys_system` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(32) NOT NULL DEFAULT '' COMMENT '系统名称',
    `create_time` bigint NOT NULL DEFAULT '0' COMMENT '创建时间',
    `update_time` bigint NOT NULL DEFAULT '0' COMMENT '更新时间',
    `is_deleted` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`, `is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统表';

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `uuid` varchar(12) NOT NULL DEFAULT '' COMMENT '系统用户编号',
    `nick_name` varchar(256) NOT NULL DEFAULT '' COMMENT '用户昵称',
    `phone` varchar(64) NOT NULL DEFAULT '' COMMENT '用户手机号',
    `password` varchar(255) NOT NULL DEFAULT '' COMMENT '密码',
    `token` varchar(1024) NOT NULL DEFAULT '' COMMENT 'token',
    `suspend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否暂停使用,0:否,1:是',
    `admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是超级管理员',
    `email` varchar(128) NOT NULL DEFAULT '' COMMENT '邮箱',
    `expiration` bigint NOT NULL DEFAULT '0' COMMENT 'token过期时间',
    `avatar_url` varchar(2048) NOT NULL DEFAULT '' COMMENT '用户头像',
    `phone_country_code` varchar(32) NOT NULL DEFAULT '' COMMENT '用户电话国家码',
    `allow_service` varchar(256) NOT NULL DEFAULT '[]' COMMENT '用户允许访问的服务',
    `last_online_time` bigint NOT NULL DEFAULT '0' COMMENT '用户上一次访问时间',
    `create_time` bigint NOT NULL DEFAULT '0' COMMENT '创建时间',
    `update_time` bigint NOT NULL DEFAULT '0' COMMENT '更新时间',
    `is_deleted` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uuid` (`uuid`) USING BTREE,
    KEY `token` (`token`(768)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(256) NOT NULL DEFAULT '' COMMENT '角色名称',
    `system_id` bigint NOT NULL DEFAULT '0' COMMENT '系统编号',
    `create_time` bigint NOT NULL DEFAULT '0' COMMENT '创建时间',
    `update_time` bigint NOT NULL DEFAULT '0' COMMENT '更新时间',
    `is_deleted` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`(128), `system_id`, `is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS `permission` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(32) NOT NULL DEFAULT '' COMMENT '权限名称',
    `url` varchar(256) NOT NULL DEFAULT '' COMMENT '权限内容',
    `create_time` bigint NOT NULL DEFAULT '0' COMMENT '创建时间',
    `update_time` bigint NOT NULL DEFAULT '0' COMMENT '更新时间',
    `is_deleted` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `system_id` bigint NOT NULL DEFAULT '0' COMMENT '所属系统编号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `url` (`url`, `system_id`, `is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `user_role_association` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL DEFAULT '0' COMMENT '用户编号',
    `role_id` bigint NOT NULL DEFAULT '0' COMMENT '角色编号',
    `create_time` bigint NOT NULL DEFAULT '0' COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `r_u` (`role_id`, `user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS `role_permission_association` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `role_id` bigint NOT NULL DEFAULT '0' COMMENT '角色编号',
    `permission_id` bigint NOT NULL DEFAULT '0' COMMENT '权限编号',
    `create_time` bigint NOT NULL DEFAULT '0' COMMENT '创建时间',
    `read_all` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否允许读取当前权限的所有内容',
    PRIMARY KEY (`id`),
    UNIQUE KEY `r_p` (`role_id`, `permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色权限关联表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS `system_key_value` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `system_id` bigint NOT NULL DEFAULT '0' COMMENT '系统编号',
    `key` varchar(32) NOT NULL DEFAULT '' COMMENT '配置名',
    `value` varchar(4096) NOT NULL DEFAULT '' COMMENT '配置值',
    `name` varchar(64) NOT NULL DEFAULT '' COMMENT '配置页显示的配置名',
    `remark` varchar(1024) NOT NULL DEFAULT '' COMMENT '备注',
    `create_time` bigint NOT NULL DEFAULT '0' COMMENT '创建时间',
    `update_time` bigint NOT NULL DEFAULT '0' COMMENT '更新时间',
    `is_deleted` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `key_system` (`key`, `system_id`, `is_deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';

-- 添加外键约束
ALTER TABLE `role`
    ADD CONSTRAINT `fk_role_system` FOREIGN KEY (`system_id`) REFERENCES `sys_system` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `permission`
    ADD CONSTRAINT `fk_permission_system` FOREIGN KEY (`system_id`) REFERENCES `sys_system` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `user_role_association`
    ADD CONSTRAINT `fk_ura_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    ADD CONSTRAINT `fk_ura_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT;

ALTER TABLE `role_permission_association`
    ADD CONSTRAINT `fk_rpa_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    ADD CONSTRAINT `fk_rpa_permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT;

ALTER TABLE `system_key_value`
    ADD CONSTRAINT `fk_skv_system` FOREIGN KEY (`system_id`) REFERENCES `sys_system` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT;

-- 初始化超级管理员系统
INSERT INTO `sys_system` (`name`, `create_time`, `update_time`) VALUES ('超级管理员系统', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000);