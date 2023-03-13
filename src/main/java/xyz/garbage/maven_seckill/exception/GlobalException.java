package xyz.garbage.maven_seckill.exception;

import xyz.garbage.maven_seckill.result.StatusCode;

public class GlobalException extends RuntimeException {

    private StatusCode statusCode;

    public GlobalException(StatusCode statusCode) {
        super(statusCode.toString());
        this.statusCode = statusCode;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }
}
