package com.juefeng.android.framework.http.base;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 10:25
 * Description:
 */
public class HttpThread extends Thread {

    private HttpRequestor httpRequestor;

    public HttpThread(HttpRequestor httpRequestor){
        this.httpRequestor = httpRequestor;
    }

    @Override
    public void run() {
        httpRequestor.request();
    }
}
