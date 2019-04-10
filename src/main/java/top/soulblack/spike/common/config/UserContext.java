package top.soulblack.spike.common.config;

import top.soulblack.spike.model.SpikeUser;

/**
 * @Author: 廉雪峰
 * @Date: 2019/4/10 18:44
 * @Version 1.0
 */
public class UserContext {

    // ThreadLocal 多线程时不冲突 跟当前线程绑定的
    private static ThreadLocal<SpikeUser> userHolder = new ThreadLocal<SpikeUser>();

    public static void setUser(SpikeUser user) {
        userHolder.set(user);
    }

    public static SpikeUser getUser() {
        return userHolder.get();
    }
}
