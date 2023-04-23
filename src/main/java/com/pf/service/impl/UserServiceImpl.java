package com.pf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pf.dao.UserMapper;
import com.pf.pojo.User;
import com.pf.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
}
