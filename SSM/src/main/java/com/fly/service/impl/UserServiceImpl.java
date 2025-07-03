package com.fly.service.impl;

import com.fly.dao.UserDao;
import com.fly.pojo.UserInfo;
import com.fly.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserInfoService {

    @Autowired
    UserDao userDao;

    @Override
    public UserInfo findByUsername(String name) {
        return userDao.findByUsername(name);
    }
}
