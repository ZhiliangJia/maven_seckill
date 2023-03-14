package xyz.garbage.maven_seckill.ampq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.garbage.maven_seckill.bean.OrderInfo;
import xyz.garbage.maven_seckill.bean.User;
import xyz.garbage.maven_seckill.service.GoodsService;
import xyz.garbage.maven_seckill.service.OrderService;
import xyz.garbage.maven_seckill.service.SeckillService;
import xyz.garbage.maven_seckill.util.RedisUtil;
import xyz.garbage.maven_seckill.vo.GoodsVo;

@Service
public class SeckillReceiver {

    private final Logger log = LoggerFactory.getLogger(SeckillReceiver.class);

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Transactional
    public OrderInfo seckill(User user, GoodsVo goodsVo) {
        boolean isSuccess = goodsService.reduceStock(goodsVo);
        if (isSuccess) return orderService.createOrder(user, goodsVo);
        else seckillService.setGoodsOver(goodsVo.getId());
        return null;
    }

    public long getSeckillResult(long userId, long goodsId) {
        return seckillService.getSeckillResult(userId, goodsId);
    }
}
