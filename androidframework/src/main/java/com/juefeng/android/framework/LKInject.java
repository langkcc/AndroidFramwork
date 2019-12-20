package com.juefeng.android.framework;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 14:01
 * Description:
 */
public interface LKInject {


    /**
     * inject view
     *
     * @param view
     */
    void inject(View view);

    /**
     * inject activity
     *
     * @param activity
     */
    void inject(Activity activity);

    /**
     * inject view holder
     *
     * @param handler view holder
     * @param view
     */
    void inject(Object handler, View view);

    /**
     * inject fragment
     *
     * @param fragment
     * @param inflater
     * @param container
     * @return
     */
    View inject(Object fragment, LayoutInflater inflater, ViewGroup container);
}
