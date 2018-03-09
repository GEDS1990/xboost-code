package com.xboost.util;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.xboost.controller.SolutionRouteController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.servlet.http.HttpServletRequest;

/**
 * redis cache 工具类
 */
public final class RedisUtil {
    /*private Logger logger = Logger.getLogger(RedisUtil.class);  */
    private RedisTemplate<Serializable, Object> redisTemplate;
    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        boolean res;
        try{
            res = redisTemplate.hasKey(key);
        }catch (Exception e){
            res = false;
        }
        return res;
    }

    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
            logger.info("----加入缓存---key="+key);
        } catch (Exception e) {
//            e.printStackTrace();
            logger.info("----加入缓存失败----连接异常---key="+key);
        }
        return result;
    }

    /**
     * 获取key
     * @return
     */
    public String getKey(HttpServletRequest request) {
        String key = ShiroUtil.getCurrentUserId()+"-"+ShiroUtil.getOpenScenariosId()+"-"+request.getRequestURI();
        return key;
    }
    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
            logger.info("----加入缓存---key="+key);
        } catch (Exception e) {
//            e.printStackTrace();
            logger.info("----加入缓存失败----连接异常---key="+key);
        }
        return result;
    }
    public RedisTemplate<Serializable, Object> getRedisTemplate() {
        return this.redisTemplate;
    }
    public void setRedisTemplate(RedisTemplate<Serializable, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
