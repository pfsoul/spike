package top.soulblack.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.soulblack.spike.mapper.OrderInfoMapper;
import top.soulblack.spike.model.OrderInfo;
import top.soulblack.spike.model.SpikeOrder;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.vo.GoodsVo;
import top.soulblack.spike.redis.RedisService;
import top.soulblack.spike.redis.key.OrderKey;

import java.util.Date;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/28 10:56
 * @Version 1.0
 */
@Service
public class OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private RedisService redisService;

    public SpikeOrder getSpikeOrderByUserIdGoodsId(Long userId, long goodsId) {
        return redisService.get(OrderKey.getSpikeOrderByUidGid, "" + userId + "_" + goodsId, SpikeOrder.class);
        //return orderInfoMapper.getSpikeOrderByUserIdGoodsId(userId, goodsId);
    }

    @Transactional
    public OrderInfo createOrder(SpikeUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSpikePrice());
        orderInfo.setOrderChannel((byte) 1);
        orderInfo.setStatus((byte) 0);
        orderInfo.setUserId(user.getId());
        // bug orderId 一直为1
        long orderId = orderInfoMapper.insert(orderInfo);
        SpikeOrder spikeOrder = new SpikeOrder();
        spikeOrder.setGoodsId(goods.getId());
        spikeOrder.setOrderId(orderId);
        spikeOrder.setUserId(user.getId());
        orderInfoMapper.insertSpikeOrder(spikeOrder);

        redisService.set(OrderKey.getSpikeOrderByUidGid, "" + user.getId() + "_" + goods.getId(), spikeOrder);

        return orderInfo;
    }


    public OrderInfo getOrderById(long orderId) {
        return orderInfoMapper.getOrderById(orderId);
    }
}
