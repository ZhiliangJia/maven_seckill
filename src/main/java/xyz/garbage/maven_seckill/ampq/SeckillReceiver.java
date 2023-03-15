package xyz.garbage.maven_seckill.ampq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.garbage.maven_seckill.bean.SeckillOrder;
import xyz.garbage.maven_seckill.bean.User;
import xyz.garbage.maven_seckill.config.WebConfig;
import xyz.garbage.maven_seckill.service.GoodsService;
import xyz.garbage.maven_seckill.service.OrderService;
import xyz.garbage.maven_seckill.service.SeckillService;
import xyz.garbage.maven_seckill.util.RedisUtil;
import xyz.garbage.maven_seckill.util.SerializeUtil;
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

    @RabbitListener(queues = WebConfig.queueName)
    public void receive(String message) {
        log.info("Receive message: " + message);
        SeckillMessage seckillMessage = SerializeUtil.string2Bean(message, SeckillMessage.class);
        User user = seckillMessage.getUser();
        long goodsId = seckillMessage.getGoodsId();

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stock = goodsVo.getStockCount();
        if (stock <= 0) return;
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) return;
        seckillService.seckill(user, goodsVo);
    }
}
