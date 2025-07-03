package com.fly.jsonredis.controller;


import com.fly.jsonredis.service.impl.RedissonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/redisson")
public class RedissonController {

    @Autowired
    private RedissonServiceImpl redissonService;

    //   get请求   http://localhost:8080/jsonRedis/redisson/getDataFromRedis?key=RedissonTest
    @GetMapping("/getDataFromRedis")
    public ResponseEntity<String> getDataFromRedis(@RequestParam String key) {
        try {
            String data = redissonService.getDataFromRedis(key);
            if (data != null) {
                return ResponseEntity.ok(data);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Key not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }

    }



}