package top.soulblack.spike.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/25 13:54
 * @Version 1.0
 */

public class ValidatorUtil {

    private static final Pattern MOBILE_PATTERN = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src) {
        if (src.isEmpty()) {
            return false;
        }
        Matcher m = MOBILE_PATTERN.matcher(src);
        return m.matches();
    }

//    public static void main(String[] args) {
//        System.out.println(isMobile("1234567891"));
//    }
}
