package com.juefeng.android.framework.common.application;

import android.app.Application;
import com.juefeng.android.framework.common.util.AppThreadPool;
import com.juefeng.android.framework.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/22
 * Time: 17:25
 * Description: base Application
 */
public class BaseApplication extends Application {

    /**
     * app opening activitys
     */
    private List<BaseActivity> activityStack;

    private static BaseApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        activityStack = new ArrayList<>();
        AppThreadPool.init();
    }

    protected void initLKUtil(){}

    protected void initAppConfig(){}

    public static BaseApplication getInstance(){
        synchronized (instance){
            if (instance==null){
                instance = new BaseApplication();
            }
        }
        return instance;
    }

    /**
     * push activity to list
     *
     * @param activity
     */
    public void pushActivity(BaseActivity activity) {
        if (activityStack == null) {
            activityStack = new ArrayList<>();
        }
        activityStack.add(0, activity);
    }

    /**
     * pull activity from list
     *
     * @param activity
     */
    public void pullActivity(BaseActivity activity) {
        if (activityStack == null) {
            return;
        }
        for (BaseActivity item : activityStack) {
            if (activity == item) {
                activityStack.remove(item);
                return;
            }
        }
    }


    /**
     * get Current Activity
     * @return
     */
    public BaseActivity getCurrentActivity() {
        if (activityStack != null && !activityStack.isEmpty()) {
            return activityStack.get(0);
        } else {
            return null;
        }
    }


    /**
     * app exit
     */
    public void exit() {
        if (activityStack != null && !activityStack.isEmpty()) {
            for (BaseActivity activity : activityStack) {
                activity.finish();
            }
        }
        activityStack.clear();
        activityStack = null;
        AppThreadPool.shutdown();
        System.exit(0);
    }

}
