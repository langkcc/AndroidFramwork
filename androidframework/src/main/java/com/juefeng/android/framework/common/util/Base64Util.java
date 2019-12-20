package com.juefeng.android.framework.common.util;

import android.util.Base64;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/30
 * Time: 17:43
 * Description:
 */
public class Base64Util {

    Base64 decoder;


    public static String encode(String str){
        String strBase64 = new String(Base64.encode(str.getBytes(), Base64.NO_WRAP));
        return strBase64;
    }

    public static String decode(String str){
       return  new String(Base64.decode(str.getBytes(), Base64.NO_WRAP));
    }

}
