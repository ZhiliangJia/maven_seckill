package xyz.garbage.maven_seckill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.garbage.maven_seckill.bean.OrderInfo;
import xyz.garbage.maven_seckill.bean.SeckillOrder;
import xyz.garbage.maven_seckill.bean.User;
import xyz.garbage.maven_seckill.mapper.OrderMapper;
import xyz.garbage.maven_seckill.redis.impl.PrefixEnum;
import xyz.garbage.maven_seckill.redis.impl.RedisKey;
import xyz.garbage.maven_seckill.util.RedisUtil;
import xyz.garbage.maven_seckill.vo.GoodsVo;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisUtil redisUtil;

    public SeckillOrder getOrderByUserIdGoodsId(long userId, long goodsId) {
        SeckillOrder seckillOrder = redisUtil.getStringValue(RedisKey.createSpecialKey(PrefixEnum.ORDER_BY_GID_UID, String.format("%d_%d", userId, goodsId)), SeckillOrder.class);
        return seckillOrder;
    }

    public OrderInfo getOrderById(long orderId) {
        return orderMapper.getOrderById(orderId);
    }

    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goodsVo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getGoodsPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderMapper.insert(orderInfo);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        orderMapper.insertSeckillOrder(seckillOrder);

        redisUtil.setStringValue(RedisKey.createSpecialKey(PrefixEnum.ORDER_BY_GID_UID, String.format("%d_%d", user.getId(), goodsVo.getId())), seckillOrder);
        return orderInfo;
    }

}
