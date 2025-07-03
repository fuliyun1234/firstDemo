package com.fly.jsonredis.initializeData;

import com.fly.jsonredis.utils.DocumentUtils;
import com.fly.jsonredis.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.util.Pool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.fly.jsonredis.utils.JsonFilter.getFilterJson;

/**
 * 加载数据 并且 放到 redis 里面
 * @author fly

 *
 */
@Component
@Slf4j
public class InitializeData implements ApplicationRunner {
    @Autowired
    private Pool<Jedis> jedisPool;
    @Autowired
    private RedisUtil redisUtil;

    public static Map<String, String> jsonMap = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String xsltString = DocumentUtils.readFile("files/json测试.json");

        Map<String, String> jsonMap1 = new HashMap<>();
        jsonMap1.put("ODS_02020001",xsltString);
        saveJsonMap("interactiveService",jsonMap1);
        log.info("初始化 redis 数据完成");

        //从redis中加载缓慢，程序启动的时候加载
        jsonMap = redisUtil.getMap("interactiveService");

    }

    //将数据放到redis
    public void saveJsonMap(String mapKey, Map<String, String> jsonMap) {
        //统计50 * 4 * 100=200 00
        for (int i = 0; i < 20000; i++) {
            //jsonMap.put("BB"+i,jsonMap.get("ODS_02020001"));
            String filterJson = getFilterJson(jsonMap.get("ODS_02020001"));
            jsonMap.put("BB"+i,filterJson);
        }
        redisUtil.setMapByPipeline(mapKey, jsonMap);

    }




    public String getJsonRule(String mapKey) {
        String keyValue = jsonMap.get(mapKey);
        if (keyValue != null) {
            return keyValue;
        }
        lock.lock();
        try {
            keyValue = jsonMap.get(mapKey);
            if (keyValue == null) {
                Map<String, String> getMap = null;
                try (Jedis jedis = jedisPool.getResource()) {
                    getMap = jedis.hgetAll(mapKey);
                }

                if (getMap == null || getMap.isEmpty()) {
                    //getMap = getMap.queryMap();
                    // 将从数据库查询到的数据放入Redis
                    if (getMap != null && !getMap.isEmpty()) {
                        saveJsonMap("interactiveService", getMap);
                    }
                }
                if (getMap != null) {
                    jsonMap.putAll(getMap);
                    keyValue = getMap.get(mapKey);
                }
            }
        } finally {
            lock.unlock();
        }
        return keyValue;
    }







}
