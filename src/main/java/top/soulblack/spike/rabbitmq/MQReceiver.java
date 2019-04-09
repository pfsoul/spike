package top.soulblack.spike.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.soulblack.spike.common.CodeMsg;
import top.soulblack.spike.common.Result;
import top.soulblack.spike.model.OrderInfo;
import top.soulblack.spike.model.SpikeOrder;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.vo.GoodsVo;
import top.soulblack.spike.redis.RedisService;
import top.soulblack.spike.service.GoodsService;
import top.soulblack.spike.service.OrderInfoService;
import top.soulblack.spike.service.SpikeService;

/**
 * @Author: 廉雪峰
 * @Date: 2019/4/9 10:31
 * @Version 1.0
 */
@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private SpikeService spikeService;

    @RabbitListener(queues = MQConfig.SPIKE_QUEUE)
    public void receiveSpike(String message) {
        log.info("receive message" + message);
        SpikeMessage spikeMessage = RedisService.stringToBean(message, SpikeMessage.class);
        SpikeUser user = spikeMessage.getUser();
        long goodsId = spikeMessage.getGoodsId();
        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        // 判断是否已经秒杀到了
        SpikeOrder order = orderInfoService.getSpikeOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        // 减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = spikeService.spike(user, goods);
    }

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        log.info("receive message" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void topicReceive1(String message) {
        log.info("receive topic1 message" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void topReceive2(String message) {
        log.info("receive topic2 message" + message);
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void headersReceive(byte[] message) {
        log.info("receive headers message" + new String(message));
    }
}
