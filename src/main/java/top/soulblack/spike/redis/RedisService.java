package top.soulblack.spike.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import top.soulblack.spike.redis.key.base.KeyPrefix;


/**
 * @Author: 廉雪峰
 * @Date: 2019/3/24 15:34
 * @Version 1.0
 */
@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    /**
     * get方法，获取单个对象
     * @param keyPrefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix keyPrefix,String key, Class<T> clazz) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = keyPrefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = stringToBean(str,clazz);
            return t;
        }finally {
            returntoPool(jedis);
        }
    }

    /**
     * set方法，设置对象
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix keyPrefix,String key, T value) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            // 生成真正的key
            String realKey = keyPrefix.getPrefix() + key;
            // 判断过期时间
            int seconds = keyPrefix.expireSeconds();
            if (seconds <= 0) {
                jedis.set(realKey, str);
            } else {
                // 设置key的过期时间
                jedis.setex(realKey, seconds, str);
            }

            return true;
        }finally {
            returntoPool(jedis);
        }
    }

    /**
     * 判断一个key是否已经存在，存在返回true，不存在返回false
     * @param keyPrefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean exist(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = keyPrefix + key;
            return jedis.exists(realKey);
        }finally {
            returntoPool(jedis);
        }
    }

    /**
     * 删除对应缓存
     * @param keyPrefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long ret = jedis.del(keyPrefix + key);
            return ret > 0;
        }finally {
            returntoPool(jedis);
        }
    }

    /**
     * key值加1，原子操作
     * @param keyPrefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = keyPrefix + key;
            return jedis.incr(realKey);
        }finally {
            returntoPool(jedis);
        }
    }

    /**
     * key值减1，原子操作
     * @param keyPrefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = keyPrefix + key;
            return jedis.decr(realKey);
        }finally {
            returntoPool(jedis);
        }
    }



    /**
     * 将bean转换成String类型存入到redis中
     * @param value
     * @param <T>
     * @return
     */
    private <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String)value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    private void returntoPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 将get到的字符串类型转换成Bean
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T stringToBean(String str,Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }


}
