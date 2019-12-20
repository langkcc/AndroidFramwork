package com.juefeng.android.framework.common.util;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/30
 * Time: 10:23
 * Description:
 */
public class StringUtil {


    public static boolean isEmpty(String value) {
        if (value == null || value.equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isUrl(String value) {
        if (value == null || value.equals("")) {
            return false;
        }
        if (value.startsWith("http://") || value.startsWith("https://")) {
            return true;
        }
        return false;
    }
}
