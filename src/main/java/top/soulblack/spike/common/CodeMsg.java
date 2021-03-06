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
    public static CodeMsg SUCCESS = new CodeMsg(1, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常:%s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法");
    public static CodeMsg VERIFYCODE_ERROR = new CodeMsg(500103, "验证码错误");
    public static CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(500103, "访问过于频繁");

    // 登录异常
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500111, "密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500112, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500113, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500114, "手机号码不存在");
    public static final CodeMsg MOBILE_EXIST = new CodeMsg(500117,"手机号码已经存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500115, "密码错误");
    public static CodeMsg NOT_LOGIN = new CodeMsg(500116, "尚未登录请先登录");

    // 订单模块
    public static CodeMsg ORDER_NOT_EXIT = new CodeMsg(500400, "订单不存在");

    // 秒杀模块
    public static CodeMsg STOCK_EMPTY = new CodeMsg(500500, "商品库存不足，秒杀失败");
    public static CodeMsg REPEATE_SPIKE = new CodeMsg(500501, "不能重复秒杀");

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
