package com.ss.minio.handler;

import com.ss.minio.exception.BaseException;
import com.ss.minio.res.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * 全局异常处理类
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String PREFIX = "";

    private static final String DEFAULT_ERROR_MESSAGE = "服务内部异常";

    private Result buildResponse(String msg) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, msg);
    }

    private Result buildResponse(HttpStatus status, String msg) {
        return buildResponse(status.value(), msg);
    }

    private Result buildResponse(Integer code, String msg) {
        String message = msg;
        if (StringUtils.hasText(msg)) {
            message = PREFIX.concat(msg);
        }
        return new Result(code, message, null);
    }

    private void displayLog(HttpServletRequest request, Exception ex, boolean isPrintStack) {
        if (isPrintStack) {
            log.error("### Stack Trace ###", ex);
        }
        StackTraceElement stackTrace = ex.getStackTrace()[0];
        log.error("\n\n---------------------------------------------------" +
                "\n-请求：  " + request.getRequestURI() +
                "\n-消息：  " + ex.getMessage() +
                "\n-文件名：" + stackTrace.getFileName() +
                "\n-类名：  " + stackTrace.getClassName() +
                "\n-方法：  " + stackTrace.getMethodName() +
                "\n-行数：  " + stackTrace.getLineNumber() +
                "\n---------------------------------------------------\n");
    }

    /**
     * -------- 自定义定异常处理方法 --------
     **/
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public Result error(HttpServletRequest request, HttpServletResponse response, BaseException e) {
        // request和response一些信息有需要可以进行处理
        displayLog(request, e, true);
        return buildResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Result bindException(HttpServletRequest request, BindException ex) {
        displayLog(request, ex, true);
        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        List<String> bindingErrorMap = new LinkedList<>();
        if (ex.getBindingResult().hasErrors()) {
            for (ObjectError error : errorList) {
                bindingErrorMap.add(error.getDefaultMessage());
            }
        }
        return buildResponse(bindingErrorMap.toString());
    }


    // 其他异常自行添加

    /**
     * -------- 通用异常处理方法 --------
     **/
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(HttpServletRequest request, Exception e) {
        displayLog(request, e, true);
        return buildResponse(e.getMessage());
    }
}
