package com.juefeng.android.framework.image;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 9:47
 * Description:
 */
public abstract class ImageReqesterHandler extends Handler {

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
     */
    abstract void onSuccess(String bitmap);

    abstract void onFailed();
}
