package com.juefeng.android.framework.down;

import android.os.Handler;
import android.os.Message;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 16:17
 * Description:
 */
public abstract class DownHandler extends Handler {

    /**
     * IAMGE load success
     */
    public static final int SUCCESS = 0;
    /**
     * IAMGE load failed
     */
    public static final int FAILED = 1;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SUCCESS:
                onSuccess((String) msg.obj);
                break;
            case FAILED:
                onFailed();
                break;
            default:
                onFailed();
        }
    }

    /**
     * http responce callback
     *
     * @param path file local path
     */
    public abstract void onSuccess(String path);

    public abstract void onFailed();

}
