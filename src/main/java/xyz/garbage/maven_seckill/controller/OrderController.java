package xyz.garbage.maven_seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.garbage.maven_seckill.bean.OrderInfo;
import xyz.garbage.maven_seckill.bean.User;
import xyz.garbage.maven_seckill.result.Result;
import xyz.garbage.maven_seckill.result.StatusCode;
import xyz.garbage.maven_seckill.service.GoodsService;
import xyz.garbage.maven_seckill.service.OrderService;
import xyz.garbage.maven_seckill.vo.GoodsVo;
import xyz.garbage.maven_seckill.vo.OrderDetailVo;

@Controller
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;


    @RequestMapping("detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, User user, @RequestParam("orderId") long orderId) {
        if (user == null) return Result.error(StatusCode.SESSION_ERROR);
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) return Result.error(StatusCode.ORDER_NOT_EXIST);
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detailVo = new OrderDetailVo();
        detailVo.setOrderInfo(order);
        detailVo.setGoodsVo(goodsVo);
        return Result.success(detailVo);
    }
}
