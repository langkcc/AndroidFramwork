package com.juefeng.android.framework.common.ex;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/25
 * Time: 14:04
 * Description:
 */
public class HttpException extends RuntimeException {

    public HttpException() {
        throw new RuntimeException("Stub!");
    }

    public HttpException(String message) {
        throw new RuntimeException(message);
    }

    public HttpException(String message, Throwable cause) {
        throw new RuntimeException(message,cause);
    }

    public HttpException(Throwable cause) {
        throw new RuntimeException(cause);
    }

}
