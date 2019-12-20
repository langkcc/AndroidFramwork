package com.juefeng.android.framework.common.util;

import android.content.Context;

public class ToastUtils{

    private static Context context;
    private static ToastCustom toastCustom;

    public static void init(Context context) {
        ToastUtils.context = context;
        ToastUtils.toastCustom = new ToastCustom(context);
    }

    public static void custom(CharSequence text) {
        if (text == null || text == "") {
            return;
        }
        if (toastCustom!=null){
            toastCustom.setShowMsg(text);
            toastCustom.show();
        }
    }

}