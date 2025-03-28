package com.watermelon.sso.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watermelon.sso.entity.User;
import com.watermelon.sso.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserManager extends ServiceImpl<UserMapper, User> {
}
