package com.zm.search.common;

import redis.clients.jedis.Jedis;

public class RedisUtil {

    private static Jedis jedisClient = null;

    private RedisUtil() {
    }

    public static Jedis getInstance() {
        if (null == jedisClient) {
            synchronized (RedisUtil.class) {
                if (null == jedisClient) {
                    jedisClient = new Jedis("localhost", 6379);
                    return jedisClient;
                }
            }
        }
        return jedisClient;
    }

}
