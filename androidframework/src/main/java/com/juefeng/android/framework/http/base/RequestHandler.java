package com.juefeng.android.framework.http.base;

import android.os.Handler;
import android.os.Message;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 9:47
 * Description:
 */
public abstract class RequestHandler extends Handler {

    /**
     * IAMGE load success
     */
    public static final int SUCCESS = 0;

    private String time;

    public static final int SUCCESSANDCRYPTO = 2;
    /**
     * IAMGE load failed
     */
    public static final int FAILED = 1;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SUCCESS:
                onSuccess((String) msg.obj, false, null);
                break;
            case FAILED:
                onFailed();
                break;
            case SUCCESSANDCRYPTO:
                onSuccess((String) msg.obj, true, time);
                break;
            default:
                onFailed();
        }
    }

    /**
     * http responce callback
     */
    protected abstract void onSuccess(String content, boolean isCrypto, String time);

    protected abstract void onFailed();

    /**
     * set request time
     *
     * @param s
     */
    public void setRequestTime(String s) {
        this.time = s;
    }
}
