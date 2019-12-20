package com.juefeng.android.framework.common.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import com.juefeng.android.framework.LKUtil;
import com.juefeng.android.framework.common.application.BaseApplication;
import com.juefeng.android.framework.common.util.PermissionManager;
import com.juefeng.android.framework.common.util.ResourceUtil;
import com.juefeng.android.framework.common.util.ToastUtils;


/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/27
 * Time: 12:36
 * Description:
 */
public class BaseActivity extends FragmentActivity {

    public static final int PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 10;
    public static final int PERMISSIONS_REQUEST_LOCALTION = 11;
    public static final int CAMERA = 12;
    public static final int CAMERA_FILE = 13;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getInstance().pushActivity(this);
        LKUtil.inject(this);
        initView();
        initData();
        initEvent();
    }

    protected void initView() {
    }

    protected void initData() {
    }

    protected void initEvent() {
    }

    protected void permisssionsResultListener(int requestCode) {
    }


    @Override
    protected void onDestroy() {
        BaseApplication.getInstance().pullActivity(this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_EXTERNAL_STORAGE:
                PermissionManager.isFirstApplyFilePermission = false;
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisssionsResultListener(requestCode);
                } else {
                    ToastUtils.custom(ResourceUtil.getString(this, "permisstion_deny_write"));
                }
                break;
            case PERMISSIONS_REQUEST_LOCALTION:
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisssionsResultListener(requestCode);
                } else {
                    ToastUtils.custom(ResourceUtil.getString(this, "permisstion_deny_local"));
                }
                break;
            case CAMERA_FILE:
                PermissionManager.isFirstApplyCaremaAndFilePermission = false;
                if (grantResults != null && grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                            || grantResults[1] == PackageManager.PERMISSION_GRANTED)
                        permisssionsResultListener(requestCode);
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        ToastUtils.custom(ResourceUtil.getString(this, "permisstion_deny_write"));
                    }
                    if (grantResults[1] == PackageManager.PERMISSION_DENIED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        ToastUtils.custom(ResourceUtil.getString(this, "permisstion_deny_camera"));
                    } else {
                        ToastUtils.custom(ResourceUtil.getString(this, "permisstion_deny"));
                    }
                } else {
                    ToastUtils.custom(ResourceUtil.getString(this, "permisstion_deny"));
                }
                break;
            default:
                if (grantResults != null && grantResults.length > 0) {
                    boolean isGrant = true;
                    for (int i : grantResults) {
                        if (i == PackageManager.PERMISSION_DENIED) {
                            isGrant = false;
                        }
                    }
                    if (isGrant) {
                        permisssionsResultListener(requestCode);
                    } else {
                        ToastUtils.custom(ResourceUtil.getString(this, "permisstion_deny"));
                    }
                } else {
                    ToastUtils.custom(ResourceUtil.getString(this, "permisstion_deny"));
                }
                break;
        }
    }

}
