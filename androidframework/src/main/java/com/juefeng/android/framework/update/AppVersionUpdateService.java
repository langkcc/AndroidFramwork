package com.juefeng.android.framework.update;

import android.app.Activity;
import com.google.gson.Gson;
import com.juefeng.android.framework.LKUtil;
import com.juefeng.android.framework.common.dialog.DialogUpdateVersion;
import com.juefeng.android.framework.http.responce.HttpAsycResponceHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 版本更新服务
 *
 * @author LangK
 */
public class AppVersionUpdateService<T extends ResultUpdateVersion> {

    private Gson gson = new Gson();

    private Activity activity;

    private String updateUrl;

    private Map<String, String> params;

    private OnUpdateListener onUpdateListener;

    private DialogUpdateVersion dialogUpdateVersion;

    Class<T> clz;

    public AppVersionUpdateService(Activity activity, String updateUrl, Map<String, String> params, Class<T> clz, OnUpdateListener onUpdateListener) {
        this.activity = activity;
        this.updateUrl = updateUrl;
        this.params = params;
        this.clz = clz;
        this.onUpdateListener = onUpdateListener;
    }


    public void update() {
        LKUtil.getHttpManager().post(updateUrl, params, handler);
    }

    private HttpAsycResponceHandler<Map<String, String>> handler = new HttpAsycResponceHandler<Map<String, String>>() {
        @Override
        public void onSuccess(final Map<String, String> map) {
            super.onSuccess(map);
            String strJson = gson.toJson(map);
            final T t = gson.fromJson(strJson, clz);
            if (t.isUpdate()) {
                if (t.isForceUpdate()) {
                    dialogUpdateVersion = new DialogUpdateVersion(activity, t.getContent(), false);
                } else {
                    dialogUpdateVersion = new DialogUpdateVersion(activity, t.getContent(), true);
                }
                dialogUpdateVersion.setOnDialogVersionListener(new DialogUpdateVersion.OnDialogVersionListener() {
                    @Override
                    public void onSure() {
                        if (onUpdateListener != null) {
                            onUpdateListener.success(t);
                        }
                    }

                    @Override
                    public void onCancel() {
                        if (onUpdateListener != null) {
                            onUpdateListener.nothing();
                        }
                    }
                });
                dialogUpdateVersion.show();
            } else {
                if (onUpdateListener != null) {
                    onUpdateListener.nothing();
                }
            }

        }

        @Override
        public void onFailed(int code, String message) {
            super.onFailed(code, message);
            if (onUpdateListener != null) {
                onUpdateListener.nothing();
            }
        }

        @Override
        public void onError() {
            super.onError();
            if (onUpdateListener != null) {
                onUpdateListener.nothing();
            }
        }
    };

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


    public OnUpdateListener getOnUpdateListener() {
        return onUpdateListener;
    }

    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }

    public interface OnUpdateListener<T> {
        void success(T t);

        void nothing();
    }

}
