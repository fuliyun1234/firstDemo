package com.fly.dao;

import com.fly.pojo.UserInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    //@Select("select * from user where name = #{username}")
    UserInfo findByUsername(String username);
}
