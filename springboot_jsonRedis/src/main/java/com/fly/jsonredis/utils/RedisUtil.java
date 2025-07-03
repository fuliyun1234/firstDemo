package com.fly.jsonredis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.Map;

@Component
public class RedisUtil {

    private final JedisPool jedisPool;
    private final int database;

    @Autowired
    public RedisUtil(JedisPool jedisPool, @Value("${spring.redis.database}") int database) {
        this.jedisPool = jedisPool;
        this.database = database;
    }

    /**
     * 单独 放key 对应的 value
     *
     * @param key
     * @param value
     */
    public void setStringValue(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            //     JedisPool 的构造函数没有直接支持数据库分区的参数，需要在使用 Jedis 连接时显式切换到指定的数据库分区
            jedis.select(database); // 切换到指定数据库分区
            jedis.set(key, value);
        }
    }

    public String getStringValue(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(database); // 切换到指定数据库分区
            return jedis.get(key);
        }
    }

    public void deleteStringValue(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(database); // 切换到指定数据库分区
            jedis.del(key);
        }
    }



    /**
     * 存放一个 Map（整个map作为一个值） 到 Redis 的哈希结构中
     * Map 数据量较小（例如几十到几百个字段）
     *
     * @param key   Redis 的键
     * @param map   要存放的 Map 数据
     */
    public void setMap(String key, Map<String, String> map) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(database); // 切换到指定数据库
            jedis.hset(key, map);   // 存放整个 Map
        }
    }

    /**
     * 同 setMap 方法，区别在于
     * Map 数据量较大（例如上千个字段）
     * 对性能要求较高，且不需要严格原子性的场景。
     *
     * @param key
     * @param map
     */
    public void setMapByPipeline(String key, Map<String, String> map) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(database); // 切换到指定数据库
            Pipeline pipeline = jedis.pipelined();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                pipeline.hset(key, entry.getKey(), entry.getValue());
            }
            pipeline.sync();
        }
    }


    /**
     * 从 Redis 的哈希结构中读取整个 Map
     *
     * @param key Redis 的键
     * @return 返回整个 Map
     */
    public Map<String, String> getMap(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(database); // 切换到指定数据库
            return jedis.hgetAll(key); // 获取整个 Map
        }
    }

    /**
     * 从 Redis 的哈希结构中读取指定字段的值 （获取map中的一个key对应的值）
     *
     * @param key   Redis 的键
     * @param field 哈希字段
     * @return 返回字段的值
     */
    public String getMapKeyValue(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(database); // 切换到指定数据库
            return jedis.hget(key, field); // 获取指定字段的值
        }
    }

    /**
     * 删除 Redis 哈希结构中的指定字段（删除map中的一个key）
     *
     * @param key   Redis 的键
     * @param field 要删除的字段
     */
    public void deleteMapField(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(database); // 切换到指定数据库
            jedis.hdel(key, field); // 删除指定字段
        }
    }


    /**
     * 删除整个 Redis 哈希结构（删除整个map）
     *
     * @param key Redis 的键
     */
    public void deleteMap(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(database); // 切换到指定数据库
            jedis.del(key); // 删除整个哈希结构
        }
    }






}