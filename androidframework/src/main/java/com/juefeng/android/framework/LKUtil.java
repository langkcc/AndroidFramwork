package com.juefeng.android.framework;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.juefeng.android.framework.common.application.BaseApplication;
import com.juefeng.android.framework.common.base.Constant;
import com.juefeng.android.framework.common.config.AppConfig;
import com.juefeng.android.framework.common.ex.LKUtilException;
import com.juefeng.android.framework.common.util.FileUtil;
import com.juefeng.android.framework.common.util.PreferencesUtils;
import com.juefeng.android.framework.common.util.UUID;
import com.juefeng.android.framework.db.LKDBManagerImpl;
import com.juefeng.android.framework.db.entitys.TableEntity;
import com.juefeng.android.framework.down.LKDownFileManagerImpl;
import com.juefeng.android.framework.http.EncryptoEntity;
import com.juefeng.android.framework.http.HttpManagerImpl;
import com.juefeng.android.framework.image.LKImageLoaderImpl;
import com.juefeng.android.framework.view.LKInjectImpl;

import java.io.File;
import java.io.IOException;


/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 14:07
 * Description:
 */
public class LKUtil {


    private static ConnectivityManager mConnectivityManager;
    private static NetworkInfo mNetworkInfo;
    /**
     * app default configuration
     */
    private static AppConfig appConfig;

    private static LKInject lkInject;

    private static LKImageLoader lkImageLoader;

    private static LKHttpManager lkHttpManager;

    private static BaseApplication application;

    private static LKDownFileManager lkDownFileManager;

    private static LKDBManager db;

    /**
     * default http request connection timeout
     */
    private static int connectionTimeOut = 3000;
    /**
     * default http request socket timeout
     */
    private static int socketTimeOut = 3000;

    private static EncryptoEntity encryptoEntity = null;


    /**
     * set secret key
     * @param key
     */
    public static void setSecretKey(String key) {
        encryptoEntity = new EncryptoEntity(true, key);
    }

    /**
     * init modules
     */
    public static void init(BaseApplication application) {
        init(application, null);
    }

    /**
     * init modules
     */
    public static void init(BaseApplication application, boolean isDebug) {
        init(application, null);
        setDebug(isDebug);
    }

    /**
     * init modules
     */
    public static void init(BaseApplication application, LKDBManager.DbConfig dbConfig, boolean isDebug) {
        init(application, dbConfig);
        setDebug(isDebug);
    }

    /**
     * init modules
     */
    public static void init(BaseApplication application, LKDBManager.DbConfig dbConfig) {
        if (application == null) {
            throw new LKUtilException("Application must not null!!!");
        }
        LKUtil.application = application;
        appConfig = new AppConfig(LKUtil.application);

        File file = new File(appConfig.getExternalCacheDir(), Constant.DEVICE_FILE);
        if (!file.exists()){
            try {
                String uuid = UUID.uuid();
                File innerFile = new File(appConfig.getInnerCacheDir(),Constant.DEVICE_FILE);
                if (innerFile.exists()){
                    uuid = FileUtil.readFile(innerFile);
                }
                file.createNewFile();
                FileUtil.writeFile(uuid,file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (lkInject == null) {
            lkInject = LKInjectImpl.registerInstance();
        }

        if (lkImageLoader == null) {
            lkImageLoader = LKImageLoaderImpl.registerInstance();
        }

        if (lkHttpManager == null) {
            lkHttpManager = HttpManagerImpl.registerInstance(socketTimeOut, connectionTimeOut);
        }

        if (encryptoEntity != null) {
            lkHttpManager.setEncrypto(encryptoEntity);
        }

        if (lkDownFileManager == null) {
            lkDownFileManager = LKDownFileManagerImpl.registerInstance();
        }

        initDb(dbConfig);

    }


    private static void initDb(LKDBManager.DbConfig dbConfig) {
        if (dbConfig == null) {
            /**
             * init db config
             */
            dbConfig = new LKDBManager.DbConfig()
                    .setDbVersion(1)
                    //set db open listener
                    .setDbOpenListener(new LKDBManager.DbOpenListener() {
                        @Override
                        public void onDbOpened(LKDBManager db) {
                            //enable multiple thread operation db
                            db.getDatabase().enableWriteAheadLogging();
                        }
                    })
                    //set db upgrade listener
                    .setDbUpgradeListener(new LKDBManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(LKDBManager db, int oldVersion, int newVersion) {
                        }
                    })
                    //set table created listener
                    .setTableCreateListener(new LKDBManager.TableCreateListener() {
                        @Override
                        public void onTableCreated(LKDBManager db, TableEntity table) {
                        }
                    });
        }
        db = LKDBManagerImpl.getInstance(dbConfig);
    }

    public static BaseApplication app() {
        return application;
    }


    public static LKImageLoader getImageLoader() {
        return lkImageLoader;
    }


    public static LKHttpManager getHttpManager() {
        return lkHttpManager;
    }


    public static LKDBManager getDBManager() {
        return db;
    }

    public static boolean isDebug() {
        return appConfig.isDEBUG();
    }

    public static AppConfig getAppConfig() {
        return appConfig;
    }

    private static void setDebug(boolean debug) {
        appConfig.setDEBUG(debug);
    }

    public static void inject(Object o) {
        if (o instanceof Activity) {
            lkInject.inject((Activity) o);
        } else if (o instanceof View) {
            lkInject.inject((View) o);
        }
    }

    public static void inject(Object o, View v) {
        lkInject.inject(o, v);
    }


    public static View inject(Fragment o, LayoutInflater l, ViewGroup vg) {
        return lkInject.inject(o, l, vg);
    }

    public static LKDownFileManager getDownFileManager() {
        return lkDownFileManager;
    }

    /**
     * check net activity
     */
    public static boolean netActivity() {
        if (mConnectivityManager == null) {
            mConnectivityManager = (ConnectivityManager) application
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        if (mConnectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] info = mConnectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].isAvailable()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * get net type
     *
     * @return
     */
    public static String getNetTypeName() {
        if (mConnectivityManager == null) {
            mConnectivityManager = (ConnectivityManager) application
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo == null) {
            return "none net";
        }
        return mNetworkInfo.getTypeName();
    }

    public static int getConnectionTimeOut() {
        return LKUtil.connectionTimeOut;
    }

    public static void setConnectionTimeOut(int connectionTimeOut) {
        if (lkHttpManager != null) {
            lkHttpManager.setConnectionTimeOut(connectionTimeOut);
        }
        LKUtil.connectionTimeOut = connectionTimeOut;
    }

    public static int getSocketTimeOut() {
        return LKUtil.socketTimeOut;
    }

    public static void setSocketTimeOut(int socketTimeOut) {
        if (lkHttpManager != null) {
            lkHttpManager.setSocketTimeOut(socketTimeOut);
        }
        LKUtil.socketTimeOut = socketTimeOut;
    }
}
