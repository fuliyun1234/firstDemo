package com.example.springboot;

import com.example.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootAopApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        userService.createUser("张三");
    }

}
