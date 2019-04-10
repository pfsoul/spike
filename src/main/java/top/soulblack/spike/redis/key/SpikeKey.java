package top.soulblack.spike.redis.key;

import top.soulblack.spike.redis.key.base.BasePrefix;

/**
 * @Author: 廉雪峰
 * @Date: 2019/4/9 17:56
 * @Version 1.0
 */
public class SpikeKey extends BasePrefix {

    public SpikeKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SpikeKey isGoodsOver = new SpikeKey(0, "go");

    public static SpikeKey getSpikePath = new SpikeKey(60, "go");
}
