package top.soulblack.spike.common;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/24 13:44
 * @Version 1.0
 */
public class CodeMsg {

    private int code;

    private String msg;

    // 通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");


    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
