package top.soulblack.spike.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.soulblack.spike.redis.RedisService;

/**
 * @Author: 廉雪峰
 * @Date: 2019/4/9 10:30
 * @Version 1.0
 */
@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send message" + msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
    }

    public void topicSend(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send topic message" + msg);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY1,msg + "1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY2,msg + "2");
    }

    public void fanoutSend(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send fanout message" + msg);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg + "1");
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg + "2");
    }

    public void headersSend(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send headers message" + msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1", "value1");
        properties.setHeader("header2", "value2");
        Message obj = new Message(msg.getBytes(), properties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
    }

    public void sendSpikeMessage(SpikeMessage spikeMessage) {
        String msg = RedisService.beanToString(spikeMessage);
        log.info("send message" + msg);
        amqpTemplate.convertAndSend(MQConfig.SPIKE_QUEUE,msg);
    }
}
