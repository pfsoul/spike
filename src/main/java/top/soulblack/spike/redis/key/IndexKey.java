package top.soulblack.spike.redis.key;

import top.soulblack.spike.redis.key.base.BasePrefix;

/**
 * Create by 廉雪峰
 * Date: 2020/4/8 15:27
 * blog: www.soulblack.top
 * Description:
 */
public class IndexKey extends BasePrefix {
    // 有效期一天
    private static final int INDEX_EXPIRE = 60;


    public IndexKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static IndexKey getGoodList = new IndexKey(INDEX_EXPIRE, "idxgl");
}
