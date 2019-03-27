package top.soulblack.spike.util;

import java.util.UUID;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/27 14:45
 * @Version 1.0
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-","");
    }
}
