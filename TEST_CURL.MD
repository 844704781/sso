# SSO管理系统CURL测试命令

## 系统管理接口

### 分页获取系统列表
```bash
curl -X GET 'http://localhost:8080/sso/systems?page=1&size=10' \
-H 'Content-Type: application/json' \
-d '{
  "data": {
    "name": "系统名称关键字"
  }
}'
```

### 创建系统
```bash
curl -X POST 'http://localhost:8080/sso/systems' \
-H 'Content-Type: application/json' \
-d '{
  "name": "系统名称"
}'
```

### 修改系统名称
```bash
curl -X PUT 'http://localhost:8080/sso/systems/1' \
-H 'Content-Type: application/json' \
-d '{
  "name": "新系统名称"
}'
```

### 删除系统
```bash
curl -X DELETE 'http://localhost:8080/sso/systems/1' \
-H 'Content-Type: application/json'
```

### 获取系统详情
```bash
curl -X GET 'http://localhost:8080/sso/systems/1' \
-H 'Content-Type: application/json'
```

### 分页获取系统下的所有角色
```bash
curl -X GET 'http://localhost:8080/sso/systems/1/roles?page=1&size=10' \
-H 'Content-Type: application/json' \
-d '{
  "data": {
    "name": "角色名称关键字"
  }
}'
```

### 分页获取系统下的所有权限
```bash
curl -X GET 'http://localhost:8080/sso/systems/1/permissions?page=1&size=10' \
-H 'Content-Type: application/json' \
-d '{
  "data": {
    "name": "权限名称关键字",
    "url": "权限URL关键字"
  }
}'
```

### 分页获取系统下的所有配置列表
```bash
curl -X GET 'http://localhost:8080/sso/systems/1/configs?page=1&size=10' \
-H 'Content-Type: application/json' \
-d '{
  "data": {
    "key": "配置键关键字",
    "name": "配置名关键字"
  }
}'
```

### 查看配置详情
```bash
curl -X GET 'http://localhost:8080/sso/configs/1' \
-H 'Content-Type: application/json'
```

### 创建配置
```bash
curl -X POST 'http://localhost:8080/sso/systems/1/configs' \
-H 'Content-Type: application/json' \
-d '{
  "key": "config_key",
  "value": "配置值",
  "name": "配置名称",
  "remark": "备注说明"
}'
```

### 修改配置
```bash
curl -X PUT 'http://localhost:8080/sso/configs/1' \
-H 'Content-Type: application/json' \
-d '{
  "key": "config_key",
  "value": "新配置值",
  "name": "新配置名称",
  "remark": "新备注说明"
}'
```

### 删除配置
```bash
curl -X DELETE 'http://localhost:8080/sso/configs/1' \
-H 'Content-Type: application/json'
```

## 用户管理接口

### 获取用户列表
```bash
curl -X GET 'http://localhost:8080/sso/users?page=1&size=10' \
-H 'Content-Type: application/json' \
-d '{
  "data": {
    "uuid": "用户编号",
    "nick_name": "用户昵称",
    "phone": "手机号",
    "email": "邮箱地址",
    "suspend": 0
  }
}'
```

### 创建用户
```bash
curl -X POST 'http://localhost:8080/sso/users' \
-H 'Content-Type: application/json' \
-d '{
  "uuid": "u1234567890",
  "nick_name": "用户昵称",
  "phone": "13800138000",
  "email": "user@example.com",
  "password": "密码",
  "avatar_url": "https://example.com/avatar.jpg",
  "phone_country_code": "+86",
  "allow_service": "[]",
  "admin": 0
}'
```

### 暂停/开启用户
```bash
curl -X PUT 'http://localhost:8080/sso/users/u1234567890/status' \
-H 'Content-Type: application/json' \
-d '{
  "suspend": 1
}'
```

### 获取用户详情
```bash
curl -X GET 'http://localhost:8080/sso/users/u1234567890' \
-H 'Content-Type: application/json'
```

### 生成用户的api-token
```bash
curl -X POST 'http://localhost:8080/sso/users/u1234567890/token' \
-H 'Content-Type: application/json' \
-d '{
  "expiration": 1680511479000
}'
```

### 管理员修改用户密码
```bash
curl -X PUT 'http://localhost:8080/sso/users/u1234567890/password' \
-H 'Content-Type: application/json' \
-d '{
  "password": "新密码"
}'
```

### 获取用户角色
```bash
curl -X GET 'http://localhost:8080/sso/users/u1234567890/roles' \
-H 'Content-Type: application/json'
```

### 获取系统所有角色(无需分页)
```bash
curl -X GET 'http://localhost:8080/sso/systems/1/all-roles' \
-H 'Content-Type: application/json'
```

### 设置用户角色
```bash
curl -X PUT 'http://localhost:8080/sso/users/u1234567890/roles' \
-H 'Content-Type: application/json' \
-d '{
  "role_ids": [1, 2, 3]
}'
```

### 获取系统所有权限
```bash
curl -X GET 'http://localhost:8080/sso/systems/1/all-permissions' \
-H 'Content-Type: application/json'
```

### 获取用户权限
```bash
curl -X GET 'http://localhost:8080/sso/users/u1234567890/permissions?system_id=1' \
-H 'Content-Type: application/json'
```

## 角色管理接口

### 分页获取系统所有角色列表
```bash
curl -X GET 'http://localhost:8080/sso/systems/1/roles?page=1&size=10' \
-H 'Content-Type: application/json' \
-d '{
  "data": {
    "name": "角色名称关键字"
  }
}'
```

### 创建角色
```bash
curl -X POST 'http://localhost:8080/sso/systems/1/roles' \
-H 'Content-Type: application/json' \
-d '{
  "name": "角色名称"
}'
```

### 删除角色
```bash
curl -X DELETE 'http://localhost:8080/sso/roles/1' \
-H 'Content-Type: application/json'
```

### 获取角色详情
```bash
curl -X GET 'http://localhost:8080/sso/roles/1' \
-H 'Content-Type: application/json'
```

### 修改角色名称
```bash
curl -X PUT 'http://localhost:8080/sso/roles/1' \
-H 'Content-Type: application/json' \
-d '{
  "name": "新角色名称"
}'
```

### 获取角色对应的权限
```bash
curl -X GET 'http://localhost:8080/sso/roles/1/permissions' \
-H 'Content-Type: application/json'
```

### 获取当前角色对应的系统的所有权限
```bash
curl -X GET 'http://localhost:8080/sso/roles/1/system-permissions' \
-H 'Content-Type: application/json'
```

### 设置当前角色的权限
```bash
curl -X PUT 'http://localhost:8080/sso/roles/1/permissions' \
-H 'Content-Type: application/json' \
-d '{
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
}'
```

## 权限管理接口

### 分页获取系统权限列表
```bash
curl -X GET 'http://localhost:8080/sso/systems/1/permissions?page=1&size=10' \
-H 'Content-Type: application/json' \
-d '{
  "data": {
    "name": "权限名称关键字",
    "url": "URL关键字"
  }
}'
```

### 创建权限
```bash
curl -X POST 'http://localhost:8080/sso/systems/1/permissions' \
-H 'Content-Type: application/json' \
-d '{
  "name": "权限名称",
  "url": "/sso/resource"
}'
```

### 删除权限
```bash
curl -X DELETE 'http://localhost:8080/sso/permissions/1' \
-H 'Content-Type: application/json'
```

### 获取权限详情
```bash
curl -X GET 'http://localhost:8080/sso/permissions/1' \
-H 'Content-Type: application/json'
```

### 修改权限信息
```bash
curl -X PUT 'http://localhost:8080/sso/permissions/1' \
-H 'Content-Type: application/json' \
-d '{
  "name": "新权限名称",
  "url": "/sso/new-resource"
}'
```

## 认证接口

### 发送邮箱验证码
```bash
curl -X POST 'http://localhost:8080/sso/auth/email/code' \
-H 'Content-Type: application/json' \
-d '{
  "email": "user@example.com"
}'
```

### 用户邮箱注册
```bash
curl -X POST 'http://localhost:8080/sso/auth/register' \
-H 'Content-Type: application/json' \
-d '{
  "email": "user@example.com",
  "verification_code": "123456",
  "password": "密码"
}'
```

### 用户邮箱登录
```bash
curl -X POST 'http://localhost:8080/sso/auth/login' \
-H 'Content-Type: application/json' \
-d '{
  "email": "user@example.com",
  "password": "密码"
}'
```

### 退出登录
```bash
curl -X POST 'http://localhost:8080/sso/auth/logout' \
-H 'Access-Token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
-H 'Refresh-Token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
-H 'Content-Type: application/json'
```

### 刷新令牌
```bash
curl -X POST 'http://localhost:8080/sso/auth/token/refresh' \
-H 'Refresh-Token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
-H 'Content-Type: application/json'
```
