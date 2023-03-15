package xyz.garbage.maven_seckill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.garbage.maven_seckill.bean.OrderInfo;
import xyz.garbage.maven_seckill.bean.SeckillOrder;
import xyz.garbage.maven_seckill.bean.User;
import xyz.garbage.maven_seckill.redis.impl.PrefixEnum;
import xyz.garbage.maven_seckill.redis.impl.RedisKey;
import xyz.garbage.maven_seckill.util.RedisUtil;
import xyz.garbage.maven_seckill.vo.GoodsVo;

@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisUtil redisUtil;

    @Transactional
    public OrderInfo seckill(User user, GoodsVo goodsVo) {
        boolean flag = goodsService.reduceStock(goodsVo);
        if (flag) return orderService.createOrder(user, goodsVo);
        else setGoodsOver(goodsVo.getId());
        return null;
    }

    public long getSeckillResult(long userId, long goodsId) {
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) return order.getOrderId();
        boolean isOver = getGoodsOver(goodsId);
        if (isOver) return -1;
        else return 0;
    }

    public void setGoodsOver(long goodsId) {
        redisUtil.setStringValue(RedisKey.createSpecialKey(PrefixEnum.SEC_KILL_GOOD_OVER, String.valueOf(goodsId)), true);
    }

    public boolean getGoodsOver(long goodsId) {
        return redisUtil.exists(RedisKey.createSpecialKey(PrefixEnum.SEC_KILL_GOOD_OVER, String.valueOf(goodsId)));
    }

}
