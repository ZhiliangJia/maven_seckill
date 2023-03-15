package xyz.garbage.maven_seckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.garbage.maven_seckill.ampq.SeckillMessage;
import xyz.garbage.maven_seckill.ampq.SeckillSender;
import xyz.garbage.maven_seckill.bean.SeckillOrder;
import xyz.garbage.maven_seckill.bean.User;
import xyz.garbage.maven_seckill.redis.impl.PrefixEnum;
import xyz.garbage.maven_seckill.redis.impl.RedisKey;
import xyz.garbage.maven_seckill.result.Result;
import xyz.garbage.maven_seckill.result.StatusCode;
import xyz.garbage.maven_seckill.service.GoodsService;
import xyz.garbage.maven_seckill.service.OrderService;
import xyz.garbage.maven_seckill.service.SeckillService;
import xyz.garbage.maven_seckill.util.RedisUtil;
import xyz.garbage.maven_seckill.vo.GoodsVo;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SeckillSender sender;

    RateLimiter rateLimiter = RateLimiter.create(10);

    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doSeckill(Model model, User user, @RequestParam("goodsId") long goodsId) {
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) return Result.error(StatusCode.ACCESS_LIMIT_REACHED);
        if (user == null) return Result.error(StatusCode.SESSION_ERROR);
        model.addAttribute("user", user);
        long stock1 = redisUtil.decr(RedisKey.createSpecialKey(PrefixEnum.GOOD_STOCK, String.valueOf(goodsId)));
        if (stock1 < 0) {
            afterPropertiesSet();
            long stock2 = redisUtil.decr(RedisKey.createSpecialKey(PrefixEnum.GOOD_STOCK, String.valueOf(goodsId)));
            if (stock2 < 0) {
                return Result.error(StatusCode.SEC_KILL_OVER);
            }
        }
        SeckillOrder seckillOrder = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (seckillOrder != null) return Result.error(StatusCode.REPEAT_SEC_KILL);
        SeckillMessage message = new SeckillMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        sender.sendSeckillMessage(message);
        return Result.success(0);
    }

    @Override
    public void afterPropertiesSet() {
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        if (goodsVoList == null) return;
        for (GoodsVo goodsVo : goodsVoList) {
            redisUtil.setStringValue(RedisKey.createSpecialKey(PrefixEnum.GOOD_STOCK, String.valueOf(goodsVo.getId())), goodsVo.getStockCount());
        }
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model, User user,
                                      @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) return Result.error(StatusCode.SESSION_ERROR);
        return Result.success(seckillService.getSeckillResult(user.getId(), goodsId));
    }
}
