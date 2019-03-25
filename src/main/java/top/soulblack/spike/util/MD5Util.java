package top.soulblack.spike.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/25 10:43
 * @Version 1.0
 */
public class MD5Util {

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String SALT = "lian1998";

    public static String inputPassToFormPass(String inputPass) {
        String str = "" + SALT.charAt(0) + SALT.charAt(2) + inputPass + SALT.charAt(5) + SALT.charAt(3);
        System.out.println(str);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt) {
        String str = "" + SALT.charAt(0) + SALT.charAt(2) + formPass + SALT.charAt(5) + SALT.charAt(3);
        return md5(str);
    }

    public static String inputPassToDBPass(String input, String saltDB) {
        String formPass = inputPassToFormPass(input);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFormPass("123456"));
        System.out.println(inputPassToDBPass("123456","lian1998"));
    }
}
