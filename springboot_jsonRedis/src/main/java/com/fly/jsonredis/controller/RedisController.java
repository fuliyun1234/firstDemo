package com.fly.jsonredis.controller;


import com.fly.jsonredis.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;
import static com.fly.jsonredis.initializeData.InitializeData.jsonMap;


@RestController
@RequestMapping("/testRedis")
public class RedisController {

    @Autowired
    private Pool<Jedis> jedisPool;
    @Autowired
    private RedisUtil redisUtil;



    //  http://localhost:8080/jsonRedis/testRedis/getMap
    @RequestMapping("/getMap")
    public String getMap2() {
        //方法1
        long start = System.nanoTime();
        String test = jsonMap.get("ODS_02020001");
        long end1 = System.nanoTime();
        long durationInMillis1 = (end1 - start) / 1_000_000; // 转换为毫秒
        System.out.println("获取key的map的key的某个值：" + durationInMillis1 + " 毫秒");

        //方法二
        long start2 = System.nanoTime();
        String test2 = redisUtil.getMapKeyValue("interactiveService","ODS_02020001");
        long end2 = System.nanoTime();
        long durationInMillis2 = (end2 - start2) / 1_000_000; // 转换为毫秒
        System.out.println("获取key的map的key的某个值：" + durationInMillis2 + " 毫秒");

        return test;


    }

    /**
     * 分布式锁用于在分布式系统中控制对共享资源的访问，确保同一时间只有一个客户端可以执行关键代码
     *
     */







}