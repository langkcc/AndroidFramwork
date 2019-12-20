package com.juefeng.android.framework.http.responce;

import android.app.Dialog;
import android.os.Handler;
import com.google.gson.Gson;
import com.juefeng.android.framework.LKUtil;
import com.juefeng.android.framework.common.dialog.DialogProcess;
import com.juefeng.android.framework.common.util.LogUtil;
import com.juefeng.android.framework.http.BaseResult;
import com.juefeng.android.framework.http.base.HttpRequestor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/22
 * Time: 17:06
 * Description: Http responce logic handler
 */
public class HttpAsycResponceHandler<T> extends BaseHttpResponceHandler<T> {

    private HttpRequestor httpRequestor;
    /**
     * on http requesting show progress's style
     */
    private Dialog progressDialog;

    /**
     * whether show progress view
     */
    private boolean isShowProgress;

    /**
     * when http request canceled,this value is true
     */
    private boolean canceled = false;

    public HttpAsycResponceHandler() {
        this(false);
    }

    /**
     * you can set whether show progress at this object init moment
     *
     * @param isShowProgress
     */
    public HttpAsycResponceHandler(boolean isShowProgress) {
        this.isShowProgress = isShowProgress;
    }

    /**
     * set progress's style on application start moment
     * suggest in application ensure only style
     *
     * @param progressDialog
     */
    public void setProgressStyle(Dialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    /**
     * dismiss progress dialog
     */
    private void dismissProgressDialog() {
        if (isShowProgress) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    /**
     * show progress dialog
     */
    private void showProgressDialog() {
        if (isShowProgress) {
            progressDialog = new DialogProcess(LKUtil.app().getCurrentActivity());
            progressDialog.show();
        }
    }

    /**
     * http start requesting
     */
    public void onStart() {
        this.showProgressDialog();
    }

    /**
     * success logic handler
     *
     * @param t
     */
    public void onSuccess(T t) {

    }

    /**
     * fail logic handler
     *
     * @param code    error code
     * @param message fail message
     */
    public void onFailed(int code, String message) {

    }

    /**
     * http error handler
     */
    public void onError() {
    }

    /**
     * http handler finish
     * you can overwrite this method,but must invoking super.onFinish
     */
    public void onFinish() {
        this.dismissProgressDialog();
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        if (canceled) {
            this.onFinish();
        }
        this.canceled = canceled;
    }

    Gson gson = new Gson();

    /**
     * parse server responce content and handle logic
     *
     * @param content
     */
    public void parseContent(String content) {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        BaseResult<T> baseResult = gson.fromJson(content, type(BaseResult.class, params));
        if (baseResult.hasSuccess()) {
            try {
                this.onSuccess(baseResult.getData());
            } catch (Exception e) {
                LogUtil.e("server return data,process error", e);
                this.onError();
            }
        } else {
            this.onFailed(baseResult.getCode(), baseResult.getMessage());
        }
    }


    private ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }


    public void setHttpRequestor(HttpRequestor httpRequestor) {
        this.httpRequestor = httpRequestor;
    }
}
