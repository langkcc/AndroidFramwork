package com.juefeng.android.framework.http.responce;

import android.os.Handler;
import com.juefeng.android.framework.http.base.HttpRequestor;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Created Date 2018/1/24
 * Time: 11:42
 * Description:
 */
public abstract class BaseHttpResponceHandler<T> extends Handler {

    /**
     * http start requesting
     */
    public abstract void onStart();

    /**
     * success logic handler
     *
     */
    public abstract void onSuccess(T o);

    /**
     * fail logic handler
     *
     * @param code    error code
     * @param message fail message
     */
    public abstract void onFailed(int code, String message);

    /**
     * http error handler
     */
    public abstract void onError();
    /**
     * http handler finish
     * you can overwrite this method,but must invoking super.onFinish
     */
    public abstract void onFinish();


    public abstract boolean isCanceled();

    public abstract void parseContent(String content);

    public abstract void setHttpRequestor(HttpRequestor requestor);
}
