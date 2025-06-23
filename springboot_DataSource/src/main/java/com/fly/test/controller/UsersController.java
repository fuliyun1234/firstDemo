package com.fly.test.controller;

import com.fly.test.entity.User;
import com.fly.test.service.PrimaryUserService;
import com.fly.test.service.SecondaryUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/UsersController")
public class UsersController {

    @Resource
    PrimaryUserService primaryUserService;

    @Resource
    SecondaryUserService secondaryUserService;


    @RequestMapping("/db1")
    public List<User> primaryDataSource(String name, String password, HttpServletRequest request) {
        System.out.println(primaryUserService.getUsers());
        return primaryUserService.getUsers();
    }

    @RequestMapping("/db2")
    public List<User> secondaryDataSource(String name, String password, HttpServletRequest request) {
        System.out.println(secondaryUserService.getUsers());
        return secondaryUserService.getUsers();
    }

}
