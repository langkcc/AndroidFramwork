package com.juefeng.android.framework.http.interfaces;

import com.juefeng.android.framework.LKHttpManager;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/25
 * Time: 9:58
 * Description: request param crypto interface
 */
public interface IHttpCryptoManager extends LKHttpManager{

    /**
     * whether crypto
     * @return
     */
    Boolean isCrypto();

    /**
     * decode ciphertext to origin content
     * @param content
     * @return
     */
    String deCrypto(String content,String time);

    /**
     * encode origin content to ciphertext
     * @param content
     * @return
     */
    String enCrypto(String content,String time);

    /**
     * get crypto text
     * @return
     */
    String cryptoText();


    /**
     * init http head for crypto flag
     * @return
     */
    Map<String,String> initHeader(String time);

}
