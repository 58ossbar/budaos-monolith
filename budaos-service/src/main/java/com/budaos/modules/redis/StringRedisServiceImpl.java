package com.budaos.modules.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class StringRedisServiceImpl implements StringRedisService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 加锁
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean secKilllock(String key, String value) {
        /**
         * setIfAbsent就是setnx
         * 将key设置值为value，如果key不存在，这种情况下等同SET命令。
         * 当key存在时，什么也不做。SETNX是”SET if Not eXists”的简写
         * */
        if(redisTemplate.opsForValue().setIfAbsent(key,value)){
            //加锁成功返回true
            return true;
        }
        //避免死锁，且只让一个线程拿到锁
        String currentValue = (String) redisTemplate.opsForValue().get(key);
        /**
         * 下面这几行代码的作用：
         * 1、防止死锁
         * 2、防止多线程抢锁
         * */
        if(! StringUtils.isEmpty(currentValue)
                && Long.parseLong(currentValue) < System.currentTimeMillis()){
            //如果锁过期了,获取上一个锁的时间
            String oldValue = (String) redisTemplate.opsForValue().getAndSet(key,value);
            //只会让一个线程拿到锁
            //如果旧的value和currentValue相等，只会有一个线程达成条件，因为第二个线程拿到的oldValue已经和currentValue不一样了
            if(! StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)){
                return true;
            }
        }
        return false;
    }

    /**
     * 解锁
     *
     * @param key
     * @param value
     */
    @Override
    public void unlock(String key, String value) {
        try {
            String currentValue = (String) redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("『redis分布式锁』解锁异常，{}", e);
        }
    }

}
