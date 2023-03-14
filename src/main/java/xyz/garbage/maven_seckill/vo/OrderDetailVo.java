package xyz.garbage.maven_seckill.vo;

import xyz.garbage.maven_seckill.bean.OrderInfo;

public class OrderDetailVo {
    private GoodsVo goodsVo;
    private OrderInfo orderInfo;

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    @Override
    public String toString() {
        return "OrderDetailVo{" +
                "goodsVo=" + goodsVo +
                ", orderInfo=" + orderInfo +
                '}';
    }
}
