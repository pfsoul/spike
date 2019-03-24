package top.soulblack.spike.redis.key;

import top.soulblack.spike.redis.key.base.BasePrefix;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/24 16:52
 * @Version 1.0
 */
public class UserKey extends BasePrefix {

    private UserKey( String prefix) {
        super(0, prefix);
    }

    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
