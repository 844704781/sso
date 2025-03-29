# SSO管理系统设计文档
> 一个统一的单点登录系统，开放Rest接口和Dubbo接口，其它服务选择自己合适的方式接入
## 支持的功能
1. 用户管理
2. 角色管理
3. 权限管理
4. 系统管理
5. 配置管理

## 数据库设计
### 系统管理模块
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
### 用户管理模块
主要用户存储和管理用户的信息
#### 用户表
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
### 用户角色权限管理模块
#### 角色表
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

#### 权限表
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
#### 用户角色表
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

#### 角色权限表
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
### 配置管理模块
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

## RESTful接口设计
### 系统管理接口
#### 分页获取系统列表
- URL: `GET /sso/systems`
- 请求参数:
  ```json
  {
    "page": 1,
    "size": 10,
    "data": {
      "name": "系统名称关键字"
    }
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "total": 100,
      "result": [
        {
          "id": 1,
          "name": "系统名称",
          "create_time": 1648975479000,
          "update_time": 1648975479000
        }
      ]
    }
  }
  ```

#### 创建系统
- URL: `POST /sso/systems`
- 请求体:
  ```json
  {
    "name": "系统名称"
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "name": "系统名称",
      "create_time": 1648975479000,
      "update_time": 1648975479000
    }
  }
  ```

#### 修改系统名称
- URL: `PUT /sso/systems/{system_id}`
- 请求体:
  ```json
  {
    "name": "新系统名称"
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "name": "新系统名称",
      "create_time": 1648975479000,
      "update_time": 1648975479000
    }
  }
  ```

#### 删除系统
- URL: `DELETE /sso/systems/{system_id}`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
  }
  ```

#### 获取系统详情
- URL: `GET /sso/systems/{system_id}`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "name": "系统名称",
      "create_time": 1648975479000,
      "update_time": 1648975479000
    }
  }
  ```

#### 分页获取系统下的所有角色
- URL: `GET /sso/systems/{system_id}/roles`
- 请求参数:
  ```json
  {
    "page": 1,
    "size": 10,
    "data": {
      "name": "角色名称关键字"
    }
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "total": 50,
      "result": [
        {
          "id": 1,
          "name": "角色名称",
          "system_id": 1,
          "create_time": 1648975479000,
          "update_time": 1648975479000
        }
      ]
    }
  }
  ```

#### 分页获取系统下的所有权限
- URL: `GET /sso/systems/{system_id}/permissions`
- 请求参数:
  ```json
  {
    "page": 1,
    "size": 10,
    "data": {
      "name": "权限名称关键字",
      "url": "权限URL关键字"
    }
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "total": 120,
      "result": [
        {
          "id": 1,
          "name": "权限名称",
          "url": "/sso/resource",
          "system_id": 1,
          "create_time": 1648975479000,
          "update_time": 1648975479000
        }
      ]
    }
  }
  ```

#### 分页获取系统下的所有配置列表
- URL: `GET /sso/systems/{system_id}/configs`
- 请求参数:
  ```json
  {
    "page": 1,
    "size": 10,
    "data": {
      "key": "配置键关键字",
      "name": "配置名关键字"
    }
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "total": 35,
      "result": [
        {
          "id": 1,
          "system_id": 1,
          "key": "config_key",
          "value": "配置值",
          "name": "配置名称",
          "remark": "备注说明",
          "create_time": 1648975479000,
          "update_time": 1648975479000
        }
      ]
    }
  }
  ```

#### 查看配置详情
- URL: `GET /sso/configs/{config_id}`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "system_id": 1,
      "key": "config_key",
      "value": "配置值",
      "name": "配置名称",
      "remark": "备注说明",
      "create_time": 1648975479000,
      "update_time": 1648975479000
    }
  }
  ```

#### 创建配置
- URL: `POST /sso/systems/{system_id}/configs`
- 请求体:
  ```json
  {
    "key": "config_key",
    "value": "配置值",
    "name": "配置名称",
    "remark": "备注说明"
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "system_id": 1,
      "key": "config_key",
      "value": "配置值",
      "name": "配置名称",
      "remark": "备注说明",
      "create_time": 1648975479000,
      "update_time": 1648975479000
    }
  }
  ```

#### 修改配置
- URL: `PUT /sso/configs/{config_id}`
- 请求体:
  ```json
  {
    "key": "config_key",
    "value": "新配置值",
    "name": "新配置名称",
    "remark": "新备注说明"
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "system_id": 1,
      "key": "config_key",
      "value": "新配置值",
      "name": "新配置名称",
      "remark": "新备注说明",
      "create_time": 1648975479000,
      "update_time": 1649061879000
    }
  }
  ```

#### 删除配置
- URL: `DELETE /sso/configs/{config_id}`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": true
  }
  ```

### 用户管理接口
#### 获取用户列表
- URL: `GET /sso/users`
- 请求参数:
  ```json
  {
    "page": 1,
    "size": 10,
    "data": {
      "uuid": "用户编号",
      "nick_name": "用户昵称",
      "phone": "手机号",
      "email": "邮箱地址",
      "suspend": 0
    }
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "total": 200,
      "result": [
        {
          "id": 1,
          "uuid": "u1234567890",
          "nick_name": "用户昵称",
          "phone": "13800138000",
          "email": "user@example.com",
          "suspend": 0,
          "admin": 0,
          "avatar_url": "https://example.com/avatar.jpg",
          "phone_country_code": "+86",
          "allow_service": "[]",
          "last_online_time": 1648975479000,
          "create_time": 1648975479000,
          "update_time": 1648975479000
        }
      ]
    }
  }
  ```

#### 创建用户
- URL: `POST /sso/users`
- 请求体:
  ```json
  {
    "uuid": "u1234567890",
    "nick_name": "用户昵称",
    "phone": "13800138000",
    "email": "user@example.com",
    "password": "密码",
    "avatar_url": "https://example.com/avatar.jpg",
    "phone_country_code": "+86",
    "allow_service": "[]",
    "admin": 0
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "uuid": "u1234567890",
      "nick_name": "用户昵称",
      "phone": "13800138000",
      "email": "user@example.com",
      "suspend": 0,
      "admin": 0,
      "avatar_url": "https://example.com/avatar.jpg",
      "phone_country_code": "+86",
      "allow_service": "[]",
      "create_time": 1648975479000,
      "update_time": 1648975479000
    }
  }
  ```

#### 暂停/开启用户
- URL: `PUT /sso/users/{uuid}/status` 
- 请求体:
  ```json
  {
    "suspend": 1
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": true
  }
  ```

#### 获取用户详情
- URL: `GET /sso/users/{uuid}`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "uuid": "u1234567890",
      "nick_name": "用户昵称",
      "phone": "13800138000",
      "email": "user@example.com",
      "suspend": 0,
      "admin": 0,
      "avatar_url": "https://example.com/avatar.jpg",
      "phone_country_code": "+86",
      "allow_service": "[]",
      "last_online_time": 1648975479000,
      "create_time": 1648975479000,
      "update_time": 1648975479000
    }
  }
  ```

#### 生成用户的api-token
- URL: `POST /sso/users/{uuid}/token`
- 请求体:
  ```json
  {
    "expiration": 1680511479000
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "expiration": 1680511479000
    }
  }
  ```

#### 管理员修改用户密码
- URL: `PUT /sso/users/{uuid}/password`
- 请求体:
  ```json
  {
    "password": "新密码"
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": true
  }
  ```

#### 获取用户角色
- URL: `GET /sso/users/{uuid}/roles`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": [
      {
        "id": 1,
        "name": "角色名称",
        "system_id": 1,
        "create_time": 1648975479000,
        "update_time": 1648975479000
      }
    ]
  }
  ```

#### 获取系统所有角色(无需分页)
- URL: `GET /sso/systems/{system_id}/all-roles`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": [
      {
        "id": 1,
        "name": "角色名称",
        "system_id": 1,
        "create_time": 1648975479000,
        "update_time": 1648975479000
      }
    ]
  }
  ```

#### 设置用户角色
- URL: `PUT /sso/users/{uuid}/roles`
- 请求体:
  ```json
  {
    "role_ids": [1, 2, 3]
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": true
  }
  ```

#### 获取系统所有权限
- URL: `GET /sso/systems/{system_id}/all-permissions`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": [
      {
        "id": 1,
        "name": "权限名称",
        "url": "/sso/resource",
        "system_id": 1,
        "create_time": 1648975479000,
        "update_time": 1648975479000
      }
    ]
  }
  ```

#### 获取用户权限
- URL: `GET /sso/users/{uuid}/permissions`
- 请求参数:
  ```json
  {
    "system_id": 1
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": [
      {
        "id": 1,
        "name": "权限名称",
        "url": "/sso/resource",
        "system_id": 1,
        "read_all": 1,
        "create_time": 1648975479000,
        "update_time": 1648975479000
      }
    ]
  }
  ```

### 角色管理接口
#### 分页获取系统所有角色列表
- URL: `GET /sso/systems/{system_id}/roles`
- 请求参数:
  ```json
  {
    "page": 1,
    "size": 10,
    "data": {
      "name": "角色名称关键字"
    }
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "total": 50,
      "result": [
        {
          "id": 1,
          "name": "角色名称",
          "system_id": 1,
          "create_time": 1648975479000,
          "update_time": 1648975479000
        }
      ]
    }
  }
  ```

#### 创建角色
- URL: `POST /sso/systems/{system_id}/roles`
- 请求体:
  ```json
  {
    "name": "角色名称"
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "name": "角色名称",
      "system_id": 1,
      "create_time": 1648975479000,
      "update_time": 1648975479000
    }
  }
  ```

#### 删除角色
- URL: `DELETE /sso/roles/{role_id}`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": true
  }
  ```

#### 获取角色详情
- URL: `GET /sso/roles/{role_id}`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "name": "角色名称",
      "system_id": 1,
      "create_time": 1648975479000,
      "update_time": 1648975479000
    }
  }
  ```

#### 修改角色名称
- URL: `PUT /sso/roles/{role_id}`
- 请求体:
  ```json
  {
    "name": "新角色名称"
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "name": "新角色名称",
      "system_id": 1,
      "create_time": 1648975479000,
      "update_time": 1649061879000
    }
  }
  ```

#### 获取角色对应的权限
- URL: `GET /sso/roles/{role_id}/permissions`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": [
      {
        "id": 1,
        "permission_id": 1,
        "role_id": 1,
        "read_all": 1,
        "permission": {
          "id": 1,
          "name": "权限名称",
          "url": "/sso/resource",
          "system_id": 1
        }
      }
    ]
  }
  ```

#### 获取当前角色对应的系统的所有权限
- URL: `GET /sso/roles/{role_id}/system-permissions`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": [
      {
        "id": 1,
        "name": "权限名称",
        "url": "/sso/resource",
        "system_id": 1,
        "create_time": 1648975479000,
        "update_time": 1648975479000
      }
    ]
  }
  ```

#### 设置当前角色的权限
- URL: `PUT /sso/roles/{role_id}/permissions`
- 请求体:
  ```json
  {
    "permissions": [
      {
        "permission_id": 1,
        "read_all": 1
      },
      {
        "permission_id": 2,
        "read_all": 0
      }
    ]
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": true
  }
  ```

### 权限管理接口
#### 分页获取系统权限列表
- URL: `GET /sso/systems/{system_id}/permissions`
- 请求参数:
  ```json
  {
    "page": 1,
    "size": 10,
    "data": {
      "name": "权限名称关键字",
      "url": "URL关键字"
    }
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "total": 120,
      "result": [
        {
          "id": 1,
          "name": "权限名称",
          "url": "/sso/resource",
          "system_id": 1,
          "create_time": 1648975479000,
          "update_time": 1648975479000
        }
      ]
    }
  }
  ```

#### 创建权限
- URL: `POST /sso/systems/{system_id}/permissions`
- 请求体:
  ```json
  {
    "name": "权限名称",
    "url": "/sso/resource"
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "name": "权限名称",
      "url": "/sso/resource",
      "system_id": 1,
      "create_time": 1648975479000,
      "update_time": 1648975479000
    }
  }
  ```

#### 删除权限
- URL: `DELETE /sso/permissions/{permission_id}`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": true
  }
  ```

#### 获取权限详情
- URL: `GET /sso/permissions/{permission_id}`
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "name": "权限名称",
      "url": "/sso/resource",
      "system_id": 1,
      "create_time": 1648975479000,
      "update_time": 1648975479000
    }
  }
  ```

#### 修改权限信息
- URL: `PUT /sso/permissions/{permission_id}`
- 请求体:
  ```json
  {
    "name": "新权限名称",
    "url": "/sso/new-resource"
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": 1,
      "name": "新权限名称",
      "url": "/sso/new-resource",
      "system_id": 1,
      "create_time": 1648975479000,
      "update_time": 1649061879000
    }
  }
  ```

### 认证接口
#### 发送邮箱验证码
- URL: `POST /sso/auth/email/code`
- 请求体:
  ```json
  {
    "email": "user@example.com"
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": true
  }
  ```

#### 用户邮箱注册
- URL: `POST /sso/auth/register`
- 请求体:
  ```json
  {
    "email": "user@example.com",
    "verification_code": "123456",
    "password": "密码"
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "uuid": "u1234567890",
      "nick_name": "",
      "email": "user@example.com",
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "expiration": 1680511479000
    }
  }
  ```

#### 用户邮箱登录
- URL: `POST /sso/auth/login`
- 请求体:
  ```json
  {
    "email": "user@example.com",
    "password": "密码"
  }
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "uuid": "u1234567890",
      "nick_name": "用户昵称",
      "email": "user@example.com",
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "expiration": 1680511479000,
      "avatar_url": "https://example.com/avatar.jpg"
    }
  }
  ```

#### 退出登录
- URL: `POST /sso/auth/logout`
- 请求头:
  ```
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": true
  }
  ```

#### 刷新令牌
- URL: `POST /sso/auth/token/refresh`
- 请求头:
  ```
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
  ```
- 响应体:
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "expiration": 1680511479000
    }
  }
  ```





