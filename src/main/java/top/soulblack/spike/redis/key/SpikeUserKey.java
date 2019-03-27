package top.soulblack.spike.redis.key;

import top.soulblack.spike.redis.key.base.BasePrefix;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/27 14:47
 * @Version 1.0
 */
public class SpikeUserKey extends BasePrefix {

    // 有效期一天
    private static final int TOKEN_EXPIRE = 3600 * 24;

    public SpikeUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SpikeUserKey token = new SpikeUserKey(TOKEN_EXPIRE, "tk");
    public static SpikeUserKey getByName = new SpikeUserKey(TOKEN_EXPIRE, "name");
}
