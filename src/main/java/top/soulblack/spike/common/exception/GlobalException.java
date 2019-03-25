package top.soulblack.spike.common.exception;

import top.soulblack.spike.common.CodeMsg;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/25 16:35
 * @Version 1.0
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
