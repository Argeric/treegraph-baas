package org.treegraph.baas.core.component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisComponent {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long liveTime) {
        stringRedisTemplate.opsForValue().set(key, value, liveTime, TimeUnit.SECONDS);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void  delete(String key){
        stringRedisTemplate.delete(key);
    }
    
    public Long rPush(String key, String item){
        Long rlt = stringRedisTemplate.opsForList().rightPush(key, item);
        return rlt;
    }
    
    public void lPop(String key){
        stringRedisTemplate.opsForList().leftPop(key);
    }
    
    public List<String> lRange(String key, long start, long end){
        return stringRedisTemplate.opsForList().range(key, start, end);
    }
    
    public void lTrim(String key, long start, long end){
        stringRedisTemplate.opsForList().trim(key, start, end);
    }

}
