package xyz.garbage.maven_seckill.result;

public enum StatusCode {

    // 成功码
    SUCCESS(0, "成功！"),
    // 通用错误码
    SERVER_ERROR(500100, "服务端异常！"), BIND_ERROR(500101, "参数校验异常！"),
    ACCESS_LIMIT_REACHED(500102, "访问高峰期，请稍等！"),
    // 登录模块
    SESSION_ERROR(500200, "Session不存在或者已经失效！"), MOBILE_NOT_EXIST(500201, "手机号不存在！"),
    PASSWORD_ERROR(500202, "密码错误！"),
    // 商品模块
    GOOD_NOT_EXIST(500300, "商品不存在！"),
    // 订单模块
    ORDER_NOT_EXIST(500400, "订单不存在"),
    // 秒杀模块
    SEC_KILL_OVER(500500, "商品已经秒杀完毕"), REPEAT_SEC_KILL(500501, "不能重复秒杀");

    private int code;
    private String message;

    private StatusCode() {
    }

    private StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CodeMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
