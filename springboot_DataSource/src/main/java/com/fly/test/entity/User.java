package com.fly.test.entity;


import lombok.Data;


@Data
public class User {
    private Long id;
    private String name;
    private String email;
    private String tenantId;

}