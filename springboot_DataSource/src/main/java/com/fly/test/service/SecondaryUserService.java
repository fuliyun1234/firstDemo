package com.fly.test.service;

import com.fly.test.annotation.DataSource;
import com.fly.test.entity.User;
import com.fly.test.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecondaryUserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> getUsers() {
        return userMapper.findAll();
    }
}