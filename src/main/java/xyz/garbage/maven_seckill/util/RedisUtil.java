package xyz.garbage.maven_seckill.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.garbage.maven_seckill.redis.IRedisKey;

import java.util.concurrent.TimeUnit;

@Service
public class RedisUtil {

    private final Logger log = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public <T> T getStringValue(IRedisKey redisKey, Class<T> clazz) {
        String value = stringRedisTemplate.opsForValue().get(redisKey.getRealKey());
        return SerializeUtil.string2Bean(value, clazz);
    }

    public <T> void setStringValue(IRedisKey redisKey, T value) {
        stringRedisTemplate.opsForValue().set(redisKey.getRealKey(), SerializeUtil.beanToString(value), redisKey.getExpireSeconds(), TimeUnit.SECONDS);
    }

    public void deleteStringKey(IRedisKey redisKey) {
        stringRedisTemplate.opsForValue().getAndDelete(redisKey.getRealKey());
    }

    public boolean exists(IRedisKey redisKey) {
        return stringRedisTemplate.hasKey(redisKey.getRealKey());
    }

    public void incr(IRedisKey redisKey) {
        try {
            Long increment = stringRedisTemplate.opsForValue().increment(redisKey.getRealKey());
        } catch (RedisSystemException exception) {
            log.error("`{}` key auto-increment has goes wrong. （{}）", redisKey.getRealKey(), exception.getMessage());
        }
    }

    public void decr(IRedisKey redisKey) {
        try {
            Long decrement = stringRedisTemplate.opsForValue().decrement(redisKey.getRealKey());
        } catch (RedisSystemException exception) {
            log.error("`{}` key auto-decrement has goes wrong. （{}）", redisKey.getRealKey(), exception.getMessage());
        }
    }


}
