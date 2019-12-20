package com.juefeng.android.framework.common.dialog;

import android.app.Dialog;
import android.content.Context;
import com.juefeng.android.framework.R;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/22
 * Time: 17:22
 * Description: show at Http requesting
 */
public class DialogProcess extends Dialog{

    public DialogProcess(Context context) {
        super(context,R.style.DialogTheme);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_progress);
    }
}
