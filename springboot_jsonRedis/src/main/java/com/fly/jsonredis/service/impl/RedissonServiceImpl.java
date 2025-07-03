package com.fly.jsonredis.service.impl;


import com.fly.jsonredis.service.RedissonService;
import com.fly.jsonredis.utils.RedisUtil;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class RedissonServiceImpl implements RedissonService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedissonClient redissonClient;

    RBloomFilter<String> bloomFilterA;
    RBloomFilter<String> bloomFilterB;
    RBloomFilter<String> bloomFilterC;

    /**
     * 缓存击穿:  热点数据过期后，大量请求同时访问数据库
     * 缓存穿透： 缓存和数据库 数据都不存在
     */
    @Override
    public String getDataFromRedis(String key) {
        /**
         * 布隆过滤器（Bloom Filter）的核心特点是：
         *
         * 判断数据不存在时，一定不存在（100% 准确）。
         * 判断数据存在时，可能存在（存在一定的误判率）。
         */
        if (!bloomFilterA.contains(key)) {
            System.out.println("key 不在布隆过滤器里面");
            return null; // 一定不存在，返回null
        }

        String value = redisUtil.getStringValue(key);
        if (value == null) { // 缓存失效
            RLock lock = redissonClient.getLock("lock:" + key); // 获取分布式锁
            try {
                if (lock.tryLock(10, 30, TimeUnit.SECONDS)) { // 尝试加锁，最多等待 10 秒，锁的持有时间为 30 秒
                    try {
                        value = redisUtil.getStringValue(key); // 双重检查
                        if (value == null) {
                            value = getDataFromDB(key); // 从数据库加载
                            redisUtil.setStringValue(key, value); // 更新缓存
                            bloomFilterA.add(key); // 将 键 添加到布隆过滤器中
                        }
                        Thread.sleep(5000); // 模拟业务逻辑执行，使其需要5秒
                    } finally {
                        lock.unlock(); // 释放锁
                    }
                } else {
                    Thread.sleep(100); // 等待重试
                    System.out.println("当前操作无法执行（因为资源被占用），正在重试");
                    return getDataFromRedis(key); // 重试
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 重新设置中断状态
                throw new RuntimeException("Thread interrupted.");
            }
        }
        return value;
    }


    public String getDataFromDB(String key) {
        HashMap hashMap = new HashMap();
        hashMap.put("key", key);
        /**
         * 初始化布隆过滤器---数据库查询到的时候 ，就把数据放到里面
         * 创建多个布隆过滤器, 然后使用对应的过滤器就行
         */
        bloomFilterA = redissonClient.getBloomFilter("bloomFilterA");
        bloomFilterA.tryInit(100000L, 0.01); // 预期元素数量 100000，误判率 1%

        bloomFilterB = redissonClient.getBloomFilter("bloomFilterB");
        bloomFilterB.tryInit(50000L, 0.03); // 预期元素数量 50000，误判率 3%

        bloomFilterC = redissonClient.getBloomFilter("bloomFilterC");
        bloomFilterC.tryInit(200000L, 0.005); // 预期元素数量 200000，误判率 0.5%

        //向布隆过滤器中添加元素
        bloomFilterA.add("elementA1");
        bloomFilterA.add("elementA2");
        bloomFilterA.add("RedissonTest");

        bloomFilterB.add("elementB1");
        bloomFilterB.add("elementB2");

        bloomFilterC.add("elementC1");
        bloomFilterC.add("elementC2");

        //Redisson 的 RBloomFilter 是基于标准布隆过滤器实现的，不支持直接删除元素---但是可以重建立
        // 删除旧的布隆过滤器
        redissonClient.getKeys().delete("bloomFilterC");
        // 创建新的布隆过滤器
        bloomFilterB = redissonClient.getBloomFilter("bloomFilterB");
        bloomFilterB.tryInit(50000L, 0.03); // 预期元素数量 50000，误判率 3%

        bloomFilterB.add("elementB1");
        bloomFilterB.add("elementB2");

        return "hello 查询 db 了，已经 向布隆过滤器中添加元素";
    }

    public static void main(String[] args) {
        HashMap hashMap = new HashMap();

        hashMap.put("key", "");
        String aa = "11";
        System.out.println(5%3);
        System.out.println(5/3);
    }



}