package top.soulblack.spike.redis.key;

import top.soulblack.spike.redis.key.base.BasePrefix;

/**
 * @Author: 廉雪峰
 * @Date: 2019/4/2 10:46
 * @Version 1.0
 */
public class GoodsKey extends BasePrefix {
    // 有效期一天
    private static final int GoodList_EXPIRE = 60;

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodList = new GoodsKey(GoodList_EXPIRE, "gl");
    public static GoodsKey getGoodDetail = new GoodsKey(GoodList_EXPIRE, "gd");

}
