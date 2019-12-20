package com.juefeng.sharelib;

import android.app.Activity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/12/9
 * Time: 14:24
 * Description: third party login auth
 */
public class LoginService implements UMAuthListener {

    private Activity activity;


    private LoginListener loginListener;

    /**
     * type of SHARE_MEDIA
     */
    private SHARE_MEDIA type;

    /**
     * @param context
     * @param type    type of SHARE_MEDIA
     */
    public LoginService(Activity context, SHARE_MEDIA type) {
        this.activity = context;
        this.type = type;
    }

    /**
     * @param context
     * @param type    type of SHARE_MEDIA
     * @param loginListener    login listener
     */
    public LoginService(Activity context, SHARE_MEDIA type, LoginListener loginListener) {
        this.activity = context;
        this.type = type;
        this.loginListener = loginListener;
    }

    /**
     * go login
     */
    public void login() {
        UMShareAPI.get(activity).getPlatformInfo(activity, type, this);
    }


    public SHARE_MEDIA getType() {
        return type;
    }

    public void setType(SHARE_MEDIA type) {
        this.type = type;
    }

    public LoginListener getLoginListener() {
        return loginListener;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        if (loginListener != null) {
            loginListener.onSuccess(map);
        }
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        if (loginListener != null) {
            loginListener.onError();
        }
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        if (loginListener != null) {
            loginListener.onCancel();
        }
    }

    public interface LoginListener {
        /**
         * 登录成功后，第三方平台会将用户资料传回， 全部会在Map data中返回
         *
         * @param map uid		用户唯一标识
         *            name		用户昵称
         *            gender	用户性别	该字段会直接返回男女
         *            iconurl	用户头像
         *            city   城市
         *            prvinice  省份
         */
        void onSuccess(Map<String, String> map);

        /**
         * 出错
         */
        void onError();

        /**
         * 用户取消
         */
        void onCancel();
    }
}
