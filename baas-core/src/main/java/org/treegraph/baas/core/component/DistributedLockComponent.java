package org.treegraph.baas.core.component;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DistributedLockComponent {
	
    private static final int DEFAULT_EXPIRE_TIME = 30;// 单个锁有效期

    @Resource(name = "redission")
    private RedissonClient redisson;

    public boolean tryLock(String key) {
        return tryLock(key, 0L, TimeUnit.SECONDS);
    }

    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        return tryLock(key, timeout, DEFAULT_EXPIRE_TIME, unit);
    }

    public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) {
    	
    	boolean isSuccess = Boolean.FALSE;
    	
        try {
            RLock lock = redisson.getLock(key);
            isSuccess = lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            log.error("[redisKey={}]tryLock err,waitTime:{},leaseTime:{},unit:{}.", key,
                    waitTime, leaseTime, unit, e);
        }
        log.info("[redisKey={}]tryLock isSuccess:{}", key, isSuccess);
        
        return isSuccess;
    }

    public void lock(String key) {
        RLock lock = redisson.getLock(key);
        lock.lock();
    }

    public void unLock(String key) {
        try {
            RLock lock = redisson.getLock(key);
            lock.unlock();
            log.info("[redisKey={}]unLock", key);
        } catch (Exception e) {
            log.error("[redisKey={}]unLock err:", key, e);
        }
    }
}
