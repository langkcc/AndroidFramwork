package com.juefeng.android.framework.common.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.telephony.TelephonyManager;
import com.juefeng.android.framework.LKUtil;
import com.juefeng.android.framework.common.base.Constant;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/12/8
 * Time: 15:56
 * Description:
 */
public class DeviceUtil {

    private static PackageManager packageManager;
    private static TelephonyManager telephonyManager;

    /**
     * 返回版本号
     * 对应build.gradle中的versionCode
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        String versionCode = null;
        try {
            packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(packInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String deviceId = null;
        try {
            if (ActivityCompat.checkSelfPermission(LKUtil.app(), Manifest.permission.READ_PHONE_STATE) == PermissionChecker.PERMISSION_GRANTED) {
                telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    deviceId = telephonyManager.getImei();
                } else {
                    deviceId = telephonyManager.getDeviceId();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId = null;
        }
        if (deviceId == null) {
            File file = new File(LKUtil.getAppConfig().getExternalCacheDir(), Constant.DEVICE_FILE);
            deviceId = FileUtil.readFile(file);
        }
        return deviceId;
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机Android API等级（22、23 ...）
     *
     * @return
     */
    public static int getBuildLevel() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     *
     * @return
     */
    public static String getBuildVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取当前App进程的id
     *
     * @return
     */
    public static int getAppProcessId() {
        return android.os.Process.myPid();
    }

}
