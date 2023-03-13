package xyz.garbage.maven_seckill.redis.impl;

import xyz.garbage.maven_seckill.redis.IRedisKey;

public class RedisKey implements IRedisKey {

    public static IRedisKey createSpecialKey(PrefixEnum prefix, String key) {
        return new RedisKey(prefix.getTime(), prefix.getValue(), key);
    }

    private int expireSeconds;
    private String prefix;
    private String key;

    private RedisKey() {
    }

    private RedisKey(int expireSeconds, String prefix, String key) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
        this.key = key;
    }

    @Override
    public int getExpireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getRealKey() {
        return prefix + key;
    }


}
