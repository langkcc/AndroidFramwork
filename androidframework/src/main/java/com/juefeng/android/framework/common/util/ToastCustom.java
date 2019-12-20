package com.juefeng.android.framework.common.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Created Date 2018/9/6
 * Time: 14:21
 * Description:
 */
public class ToastCustom extends Toast {

    private Context context;

    private TextView tipText;


    public ToastCustom(Context context) {
        super(context);
        this.context = context;
        setGravity(Gravity.BOTTOM, 0, (int) DensityUtils.dpToPx(context, 65f));
        setDuration(Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(context).inflate(ResourceUtil.getLayout(context, "view_custom_toast"), null);
        tipText = view.findViewById(ResourceUtil.getId(context, "tv_common_custom_toast"));
        setView(view);
    }

    public void setShowMsg(CharSequence text) {
        if (tipText != null) {
            tipText.setText(text);
        }
    }


}
