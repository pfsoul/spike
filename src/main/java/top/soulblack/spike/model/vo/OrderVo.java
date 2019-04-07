package top.soulblack.spike.model.vo;

import top.soulblack.spike.model.OrderInfo;

/**
 * @Author: 廉雪峰
 * @Date: 2019/4/7 15:48
 * @Version 1.0
 */
public class OrderVo {
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
}
