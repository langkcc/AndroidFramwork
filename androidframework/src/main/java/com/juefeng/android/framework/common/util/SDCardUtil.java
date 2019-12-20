package com.juefeng.android.framework.common.util;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import com.juefeng.android.framework.LKUtil;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/11/22
 * Time: 10:37
 * Description:
 */
public class SDCardUtil {


    /**
     * 检查sd卡是否可用
     * getExternalStorageState 获取状态
     * Environment.MEDIA_MOUNTED 直译  环境媒体登上  表示，当前sd可用
     */
    public static boolean checkSdCard(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED) {
            //sd卡可用
            return true;
        } else {
            //当前sd卡不可用
            return false;
        }

    }

    /**
     * 获取外部图片缓存路径
     *
     * @param context
     * @return
     */
    public static String getExternalImageCacheDir(Context context) {
        File file = new File(Environment.getExternalStorageDirectory(),
                "Android/data/" + context.getPackageName() + "/cache/image/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    /**
     * 获取外部下载缓存路径
     *
     * @param context
     * @return
     */
    public static String getExternalDownCacheDir(Context context) {
        File file = new File(Environment.getExternalStorageDirectory(),
                "Android/data/" + context.getPackageName() + "/cache/image/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    /**
     * 获取外部缓存路径
     *
     * @param context
     * @return
     */
    public static String getExternalCacheDir(Context context) {
        File file = new File(Environment.getExternalStorageDirectory(),
                "Android/data/" + context.getPackageName() + "/cache/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }
}
