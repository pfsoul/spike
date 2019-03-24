package top.soulblack.spike.redis.key.base;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/24 16:47
 * @Version 1.0
 */
public interface KeyPrefix {
    public int expireSeconds();

    public String getPrefix();


}
