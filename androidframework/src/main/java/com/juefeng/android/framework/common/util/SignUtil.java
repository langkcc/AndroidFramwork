package com.juefeng.android.framework.common.util;


public class SignUtil
{

    public static String sign(String content)
    {
        try
        {
            return MD5.getMessageDigest(content.getBytes()).toUpperCase();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
