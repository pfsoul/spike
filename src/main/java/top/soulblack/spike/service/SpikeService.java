package top.soulblack.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.soulblack.spike.mapper.UserMapper;
import top.soulblack.spike.model.OrderInfo;
import top.soulblack.spike.model.SpikeOrder;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.User;
import top.soulblack.spike.model.vo.GoodsVo;
import top.soulblack.spike.redis.RedisService;
import top.soulblack.spike.redis.key.SpikeKey;
import top.soulblack.spike.util.MD5Util;
import top.soulblack.spike.util.UUIDUtil;

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

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo spike(SpikeUser user, GoodsVo goods) {
        //减库存 下订单，写入秒杀订单
        boolean success = spikeGoodsService.reduceStock(goods);
        if (success) {
            return orderInfoService.createOrder(user, goods);
        }
        setGoodsOver(goods.getId());
        return null;
    }


    public long getSpikeReesult(long id, long goodsId) {
        SpikeOrder order = orderInfoService.getSpikeOrderByUserIdGoodsId(id, goodsId);
        if (order != null) {
            return order.getOrderId();
        } else {
            // 判断是否秒杀成功 即物品是否卖光
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                // 秒杀失败
                return -1;
            } else {
                // 等待中
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SpikeKey.isGoodsOver, "" + goodsId, true);
    }


    private boolean getGoodsOver(long goodsId) {
        return redisService.exist(SpikeKey.isGoodsOver, "" + goodsId);
    }

    /**
     * 创造随机的秒杀路径
     * @param user
     * @param goodsId
     * @return
     */
    public String createSpikePath(SpikeUser user, long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(SpikeKey.getSpikePath, "" + user.getId() + "_" + goodsId, str);
        return str;
    }

    /**
     * 验证随机秒杀路径
     * @param path
     * @param user
     * @param goodsId
     * @return
     */
    public boolean checkPath(String path, SpikeUser user, long goodsId) {
        if (user == null || path == null) {
            return false;
        }
        String needPath = redisService.get(SpikeKey.getSpikePath, "" + user.getId() + "_" + goodsId, String.class);
        return path.equals(needPath);
    }
}
