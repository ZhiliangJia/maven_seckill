package xyz.garbage.maven_seckill.redis.impl;

public enum PrefixEnum {
    // Good商品缓存信息
    GOOD_LIST("GoodKey:gl_", 60), GOOD_DETAIL("GoodKey:gd_", -1), GOOD_STOCK("GoodKey:gs_", -1),
    // Order订单缓存信息
    ORDER_BY_GID("OrderKey:obg_", 120),
    // SecKillGood秒杀商品信息
    SEC_KILL_GOOD_Number("SecKillGoodKey:gn_", -1),
    // User用户缓存信息
    USER_TOKEN("UserKey:ut_", 60 * 60 * 24), USER_BY_ID("UserKey:uid_", 60),
    // Default信息
    DEFAULT("DefaultKey:", 60);

    private final String value;
    private final Integer time;

    PrefixEnum(String value) {
        this(value, -1);
    }

    PrefixEnum(String value, Integer time) {
        this.value = value;
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public Integer getTime() {
        return time;
    }
}
