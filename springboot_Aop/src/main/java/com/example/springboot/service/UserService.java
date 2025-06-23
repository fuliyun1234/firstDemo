package com.example.springboot.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    public void createUser(String username) {
        System.out.println("service层createUser方法: " + username);
    }
}
