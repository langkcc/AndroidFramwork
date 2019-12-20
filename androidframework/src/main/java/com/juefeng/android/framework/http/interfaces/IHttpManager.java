package com.juefeng.android.framework.http.interfaces;

import com.juefeng.android.framework.http.HttpCryptoManager;
import com.juefeng.android.framework.http.base.HttpRequestor;
import com.juefeng.android.framework.http.responce.BaseHttpResponceHandler;
import com.juefeng.android.framework.http.responce.HttpAsycResponceHandler;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/25
 * Time: 9:59
 * Description:Http request manager interface
 */
public interface IHttpManager {


    /**
     * send net request
     *
     * @param url     http url
     * @param content http post body
     * @param handler responce handler
     */
    void sendRequest(String url, Map<String, String> headMap, Map<String, String> params, String content, HttpRequestor.Method method, HttpAsycResponceHandler handler);


    /**
     * handler http responce success logic
     *
     * @param content
     * @param handler
     */
     void handlerSuccess(String content, BaseHttpResponceHandler handler);

    /**
     * handler http responce error logic
     *
     * @param handler
     */
    void handlerFailed(BaseHttpResponceHandler handler);


}
