package xyz.garbage.maven_seckill.result;

public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    public static <T> Result<T> error(StatusCode statusCode, Object... args) {
        return new Result<T>(statusCode.getCode(), String.format(statusCode.getMessage(), args));
    }

    private Result(T data) {
        this.data = data;
    }

    private Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Result(StatusCode statusCode) {
        if (statusCode == null) statusCode = StatusCode.SERVER_ERROR;
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
