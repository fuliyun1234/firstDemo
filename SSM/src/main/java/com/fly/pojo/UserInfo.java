package com.fly.pojo;

import lombok.Data;

@Data
public class UserInfo {
    private Integer id;
    private String name;
    private String password;;
    private int age;


    public UserInfo(Integer id, String name, String password, int age) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.age = age;
    }
}
