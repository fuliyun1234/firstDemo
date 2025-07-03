package com.fly.service;

import com.fly.pojo.UserInfo;
import java.util.List;

public interface UserInfoService {

 //根据用户名查询
    UserInfo findByUsername(String username);
}
