package com.example.springboot;

import com.example.springboot.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringbootAopApplication {

    public static void main(String[] args) {
        //SpringApplication.run(SpringbootAopApplication.class, args);
        ApplicationContext context = SpringApplication.run(SpringbootAopApplication.class, args);
        UserService userService = context.getBean(UserService.class);
        userService.createUser("张三");
    }

}
