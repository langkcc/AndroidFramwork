package com.juefeng.android.framework;

import com.juefeng.android.framework.http.EncryptoEntity;
import com.juefeng.android.framework.http.request.BaseFileAttachment;
import com.juefeng.android.framework.http.request.LKRequestParams;
import com.juefeng.android.framework.http.responce.HttpAsycResponceHandler;
import com.juefeng.android.framework.http.responce.HttpUploadResponceHandler;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 13:55
 * Description:
 */
public interface LKHttpManager
{

    /**
     * send post request
     * @param url
     * @param params
     * @param handler
     */
    void post(String url, Map<String,String> params, HttpAsycResponceHandler handler);

    /**
     * send post request
     * @param url
     * @param handler
     */
    void post(String url, HttpAsycResponceHandler handler);

    /**
     * send post request and params will by body sent to server
     * @param url
     * @param body
     * @param handler
     */
    void postBody(String url, Map body, HttpAsycResponceHandler handler);

    /**
     * update file
     * @param url
     * @param listFile
     * @param handler
     */
    void updateFile(String url, BaseFileAttachment listFile, HttpUploadResponceHandler handler);

    /**
     * send get request
     * @param url
     * @param handler
     */
    void get(String url, HttpAsycResponceHandler handler);

    /**
     * send get request
     * @param url
     * @param params
     * @param handler
     */
    void get(String url,Map<String,String> params, HttpAsycResponceHandler handler);


    /**
     * set request connection timeout
     * @param connectionTimeOut
     */
    void setConnectionTimeOut(int connectionTimeOut);

    /**
     * set service process timeout
     * @param socketTimeOut
     */
    void setSocketTimeOut(int socketTimeOut);
    /**
     * check crypto info
     */
    void setEncrypto(EncryptoEntity encryptoEntity);
}
