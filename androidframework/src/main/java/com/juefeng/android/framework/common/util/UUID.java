package com.juefeng.android.framework.common.util;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/12/8
 * Time: 17:12
 * Description:
 */
public class UUID {

    public static String uuid(){
        return java.util.UUID.randomUUID().toString();
    }
}
