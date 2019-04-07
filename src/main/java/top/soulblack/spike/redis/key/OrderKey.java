package top.soulblack.spike.redis.key;

import top.soulblack.spike.redis.key.base.BasePrefix;

/**
 * @Author: 廉雪峰
 * @Date: 2019/4/7 16:45
 * @Version 1.0
 */
public class OrderKey extends BasePrefix {

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getSpikeOrderByUidGid = new OrderKey(0, "soug");
}
