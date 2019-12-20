package com.juefeng.android.framework.common.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import com.juefeng.android.framework.common.application.BaseApplication;
import com.juefeng.android.framework.common.base.BaseActivity;
import com.juefeng.android.framework.common.base.BaseFragment;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Created Date 2018/9/6
 * Time: 14:07
 * Description:
 */
public class PermissionManager {

    public static Boolean isFirstApplyFilePermission = true;
    public static Boolean isFirstApplyCaremaPermission = true;
    public static Boolean isFirstApplyCaremaAndFilePermission = true;

    public static Boolean checkFilePermission(Fragment fragment) {
        if (ContextCompat.checkSelfPermission(BaseApplication.getInstance().getCurrentActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    BaseFragment.PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
            return false;
        } else {
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Boolean checkFilePermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(BaseApplication.getInstance().getCurrentActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    BaseFragment.PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
            return false;
        } else {
            return true;
        }
    }

    public static Boolean checkCamera(Fragment fragment) {
        if (ContextCompat.checkSelfPermission(BaseApplication.getInstance().getCurrentActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            fragment.requestPermissions(new String[]{Manifest.permission.CAMERA},
                    BaseFragment.CAMERA);
            return false;
        } else {
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Boolean checkCamera(Activity activity) {
        if (ContextCompat.checkSelfPermission(BaseApplication.getInstance().getCurrentActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                    BaseActivity.CAMERA);
            return false;
        } else {
            return true;
        }
    }

    public static Boolean checkCameraAndFile(Fragment fragment) {
        if (ContextCompat.checkSelfPermission(BaseApplication.getInstance().getCurrentActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(BaseApplication.getInstance().getCurrentActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    BaseFragment.CAMERA_FILE);
            return false;
        } else {
            return true;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Boolean checkCameraAndFile(Activity activity) {
        if (ContextCompat.checkSelfPermission(BaseApplication.getInstance().getCurrentActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(BaseApplication.getInstance().getCurrentActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    BaseActivity.CAMERA_FILE);
            return false;
        } else {
            return true;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Boolean checkPermisstion(Activity activity, String[] strs, int requestCode) {
        if (ContextCompat.checkSelfPermission(BaseApplication.getInstance().getCurrentActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(BaseApplication.getInstance().getCurrentActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(strs, requestCode);
            return false;
        } else {
            return true;
        }
    }

    public static Boolean checkPermisstion(Fragment fragment, String[] strs, int requestCode) {
        if (ContextCompat.checkSelfPermission(BaseApplication.getInstance().getCurrentActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(BaseApplication.getInstance().getCurrentActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            fragment.requestPermissions(strs, requestCode);
            return false;
        } else {
            return true;
        }
    }
}
