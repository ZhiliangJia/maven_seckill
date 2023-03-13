package xyz.garbage.maven_seckill.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.garbage.maven_seckill.result.Result;
import xyz.garbage.maven_seckill.result.StatusCode;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception exception) {
        log.error(exception.getMessage());
        if (exception instanceof GlobalException) {
            return Result.Error(((GlobalException) exception).getStatusCode());
        } else if (exception instanceof BindException) {
            List<ObjectError> errors = ((BindException) exception).getAllErrors();
            return Result.Error(StatusCode.BIND_ERROR, errors.get(0).getDefaultMessage());
        } else {
            return Result.Error(StatusCode.SERVER_ERROR);
        }
    }
}
