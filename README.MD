# SSO管理系统
> 一个统一的单点登录系统，开放Rest接口和Dubbo接口，其它服务选择自己合适的方式接入
## 支持的功能
1. 用户管理
2. 角色管理
3. 权限管理
4. 系统管理
5. 配置管理

## 系统管理模块
主要用于管理系统的角色
```sql
CREATE TABLE `sys_system` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '角色名称',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `is_deleted` bigint(20) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`,`is_deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='系统表'
```
## 用户管理模块
主要用户存储和管理用户的信息
### 用户表
```sql
CREATE TABLE `user`
(
    `id`                 bigint(20) NOT NULL AUTO_INCREMENT,
    `uuid`               varchar(12)   NOT NULL DEFAULT '' COMMENT '系统用户编号',
    `nick_name`          varchar(256)  NOT NULL DEFAULT '' COMMENT '用户昵称',
    `phone`              varchar(64)   NOT NULL DEFAULT '' COMMENT '用户手机号',
    `password`           varchar(255)  NOT NULL DEFAULT '' COMMENT '密码',
    `token`              varchar(1024) NOT NULL DEFAULT '' COMMENT 'token',
    `suspend`            tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否暂停使用,0:否,1:是',
    `admin`              tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是超级管理员',
    `email`              varchar(128)  NOT NULL DEFAULT '' COMMENT '邮箱',
    `expiration`         bigint(20) NOT NULL DEFAULT '0' COMMENT 'token过期时间',
    `avatar_url`         varchar(2048) NOT NULL DEFAULT '' COMMENT '用户头像',
    `phone_country_code` varchar(32)   NOT NULL DEFAULT '' COMMENT '用户电话国家码',
    `allow_service`      varchar(256)  NOT NULL DEFAULT '[]' COMMENT '用户允许访问的服务',
    `last_online_time`   bigint(20) NOT NULL DEFAULT '0' COMMENT '用户上一次访问时间',
    `create_time`        bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
    `update_time`        bigint(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
    `is_deleted`         bigint(22) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uuid` (`uuid`) USING BTREE,
    KEY                  `token` (`token`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统用户表'
```
## 用户角色权限管理模块
### 角色表
管理每个系统的角色
```sql
CREATE TABLE `role`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `name`        varchar(256) NOT NULL DEFAULT '' COMMENT '角色名称',
    `system_id`   bigint(20) NOT NULL DEFAULT '0' COMMENT '系统编号',
    `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
    `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
    `is_deleted`  bigint(20) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`,`system_id`,`is_deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=118 DEFAULT CHARSET=utf8 COMMENT='角色表'
```

### 权限表
```sql
CREATE TABLE `permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '权限名称',
  `url` varchar(256) NOT NULL DEFAULT '' COMMENT '权限内容',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `is_deleted` bigint(20) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `system_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属系统编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `url` (`url`,`system_id`,`is_deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=197 DEFAULT CHARSET=utf8 COMMENT='权限表'
```
### 用户角色表
```sql
CREATE TABLE `user_role_association` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户编号',
  `role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色编号',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `r_u` (`role_id`,`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=823 DEFAULT CHARSET=utf8 COMMENT='用户角色关联表'
```

### 角色权限表
```sql
CREATE TABLE `role_permission_association` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色编号',
  `permission_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '权限编号',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `read_all` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否允许读取当前权限的所有内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `r_p` (`role_id`,`permission_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4180 DEFAULT CHARSET=utf8 COMMENT='角色权限关联表'
```
## 配置管理模块
管理每个系统的配置
```sql
CREATE TABLE `system_key_value`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `system_id`   bigint(20) NOT NULL DEFAULT '0' COMMENT '系统编号',
    `key`         varchar(32)   NOT NULL DEFAULT '' COMMENT '配置名',
    `value`       varchar(4096) NOT NULL DEFAULT '' COMMENT '配置值',
    `name`        varchar(64)   NOT NULL DEFAULT '' COMMENT '配置页显示的配置名',
    `remark`      varchar(1024) NOT NULL DEFAULT '' COMMENT '备注',
    `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
    `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新时间',
    `is_deleted`  bigint(20) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`key`,`system_id`,`is_deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='系统配置表'
```