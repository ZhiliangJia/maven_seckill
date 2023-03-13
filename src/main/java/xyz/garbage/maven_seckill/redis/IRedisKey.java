package xyz.garbage.maven_seckill.redis;

public interface IRedisKey {

    public abstract int getExpireSeconds();

    public abstract String getPrefix();

    public abstract String getRealKey();
}
