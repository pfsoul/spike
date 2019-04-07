package top.soulblack.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.soulblack.spike.model.OrderInfo;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.vo.GoodsVo;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/28 11:01
 * @Version 1.0
 */
@Service
public class SpikeService {

    @Autowired
    private SpikeGoodsService spikeGoodsService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Transactional
    public OrderInfo spike(SpikeUser user, GoodsVo goods) {
        //减库存 下订单，写入秒杀订单
        spikeGoodsService.reduceStock(goods);
        return orderInfoService.createOrder(user, goods);
    }
}
