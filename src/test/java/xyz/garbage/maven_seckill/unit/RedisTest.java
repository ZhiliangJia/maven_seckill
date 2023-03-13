package xyz.garbage.maven_seckill.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import xyz.garbage.maven_seckill.redis.impl.PrefixEnum;
import xyz.garbage.maven_seckill.redis.impl.RedisKey;
import xyz.garbage.maven_seckill.util.RedisUtil;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void demo01() {
        redisTemplate.opsForValue().set("Demo1", "Demo1");
    }

    @Test
    public void demo02() {
        System.out.println((String) redisTemplate.opsForValue().get("Demo1"));
    }

    @Test
    public void demo03() {
        redisUtil.setStringValue(RedisKey.createSpecialKey(PrefixEnum.USER_BY_ID, "key1"), "value");
    }

    @Test
    public void demo04() {
        String key1 = redisUtil.getStringValue(RedisKey.createSpecialKey(PrefixEnum.USER_BY_ID, "key1"), String.class);
        System.out.println(key1);
    }

    @Test
    public void demo05() {
        boolean key1 = redisUtil.exists(RedisKey.createSpecialKey(PrefixEnum.USER_BY_ID, "key1"));
        System.out.println(key1);
    }

    @Test
    public void demo06() {
        redisUtil.deleteStringKey(RedisKey.createSpecialKey(PrefixEnum.USER_BY_ID, "key1"));
    }

    @Test
    public void demo07() {
        redisUtil.incr(RedisKey.createSpecialKey(PrefixEnum.USER_BY_ID, "key2"));
    }
}
