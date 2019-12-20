package com.juefeng.android.framework.common.config;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.juefeng.android.framework.common.application.BaseApplication;
import com.juefeng.android.framework.common.util.SDCardUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/27
 * Time: 15:35
 * Description:Never new this Class! AppConfig unify init on BaseApplication
 */
public class AppConfig {



    private String priKey ="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCDLb+M0s9nllurHISkK+XE/1eXkq/r6/zO+ZAJMG4VUwCTzLmIobAPCMbs9iNpCgn64hEn0wk78pGJ78Lb7CmEdtIGwXFphBb5hgDchJOZDoJnnIqX7StYh5KRvZt68EDxaCRR7tJdf8jzrX5tDZRiUpUu/c9rUMYQ8fv6XG26FaHMJMbdD6KWrzhGJEMoPmxRXxbAJGd5AkVc0UxONkWrwCuCxJ3bX/qqAbukObs3jtt7nSx5U3OfzAd27FgmUBEbjaCQJjcRFZmnmUNoBnKOn6gFAG2iSZmlzg0ZRJ2Cd4rH7QBadflwDAqjzkIO1NkGam05jvQs0OEoO4NW/gVfAgMBAAECggEAatr0QvliW8Y+K9eSMM1VYLCDIIQr66kTCWJ/Mw0RTyuKt9q3c5YJ/WUv7czesWytDoHiaaddyUhhx5PYDh+E/lNv6HLhOIuRz3aZ8wfH15DbJx18RB+9BGODQFbMdUuKC1vC4j8Be4phHiZAawL50VHmFh5IPOnSPfFwSdPKyh4uZvzwyux/1YhTQD+kY+8iEeF2OyiR824w9OP1KDStXWF2yNjZI+gOo1XpfCnFafzh005fwU+W3LiRPOdfvLy8csUOfAwI7fngMppAnkdhyyIzTZuEdwm+oouy7Njse8SycnIvWXwBWgln78AcRqh2Z0k2CF75hsDhrJg8bRTAuQKBgQDfucXMgWgUYsqJwBHF/JaNJTKjXukD4V2b9Jif04v+80GwHXKwlEo3PRWpBebC61vDdOyDP4KSgmhRLatVofLT80BQdSV3OHRCh1/zVN+jn2wRfbBmKmpGHOwtVuwGCA2QSTvxzfKhh2DMW1gUaM96mSSOT+lNPkZgcjWR09a6rQKBgQCWGjM+0TEqBJw+gGFyOYpj0RkDvHbteSJteqtlx8yZQg8pb9fBdEBi/TARIawkPRfBpYGoHCtd6s5Yt4jZy1tvWRUtYb8Nm42ASXKHp7xTv7QcanDOGUhyZDN4NKYbZl0dx0af+quU/exPxCNt9wVDWAlBhYUhZtNj3pZpt0htuwKBgQDLPIjKSbhR5JjXYCsFL3tYhzUkIiENjFosRZusfVGM7lB6+5VngZ6V5aDapejGo2X9/iao6DXmgB7ht9oQdYkRL8X6ESFS128NzMrGUxDCJB3ZGdP4S4m1XarTWUYaCJDBxTV2aT+Dq2as3kH6cGgimCIUQdXPfMYT8UxmyAkmFQKBgEI37MOZpgbTYdpw6IxrthGy/UYYrp61AesqYtLUAf7albXCEXzWvFDvQXVNVZPnH8PeMATp6HfWTpH05fqvK/dDZYRPyulF5nbh8BTIety3hc3DZ2CE5uEQM2SkVWNhFVM9xnptK6TfIJFQBhFNGJVNEUhGjyTdKAS2/cRFhz5hAoGAHJ2LZq9bOrKsLvbVMcFWnDjeaI7RuNtvdep+07sRUoLoQ+KxIpz+AJKtXOEgkzdb9Cs3XOyVSfpl5tXmJLe6Hz1WZ3vw55zPF0t/yBMz/rLXJ+6EEV8o/pMJpE0eNi2Jhm66cl9lS0M+1A2CtEOzktshTen7lhCrpzSBgixZe7s=";

    private Context context;

    public AppConfig(Context context) {
        this.context = context;
    }

    /**
     * cache file dir
     */
    private File externalCacheDir;

    /**
     * image cache file dir
     */
    private File externalImageCacheDir;

    /**
     * image cache file dir
     */
    private File externalDownCacheFile;

    /**
     * image cache file dir
     */
    private File cryptoCacheFile;

    /**
     * whether debug environment
     */
    private boolean DEBUG = false;


    public boolean isDEBUG() {
        return DEBUG;
    }

    public void setDEBUG(boolean DEBUG) {
        this.DEBUG = DEBUG;
    }

    public File getExternalCacheDir() {
        if (externalCacheDir == null || !externalCacheDir.exists()) {
            if (SDCardUtil.checkSdCard(context)) {
                this.externalCacheDir = new File(SDCardUtil.getExternalCacheDir(context));
            } else {
                this.externalCacheDir = this.context.getCacheDir();
                externalCacheDir.mkdirs();
            }
        }
        return externalCacheDir;
    }



    public File getExternalImageCacheDir() {
        if (externalImageCacheDir == null || !externalImageCacheDir.exists()) {
            if (SDCardUtil.checkSdCard(context)) {
                this.externalImageCacheDir = new File(SDCardUtil.getExternalImageCacheDir(context));
            } else {
                this.externalImageCacheDir = new File(this.context.getCacheDir(),
                        "/image/");
                externalImageCacheDir.mkdirs();
            }
        }
        return externalImageCacheDir;
    }



    public File getExternalDownCacheDir() {
        if (externalDownCacheFile == null || !externalDownCacheFile.exists()) {
            if (SDCardUtil.checkSdCard(context)) {
                this.externalDownCacheFile = new File(SDCardUtil.getExternalDownCacheDir(context));
            } else {
                this.externalDownCacheFile = new File(this.context.getCacheDir(),
                        "/down/");
                externalDownCacheFile.mkdirs();
            }
        }
        return externalDownCacheFile;
    }

    /**
     * get inner cache dir
     * @return
     */
    public File getInnerCacheDir() {
        return this.context.getCacheDir();
    }

    public File getCryptoCacheFile() {
        if (cryptoCacheFile == null) {
            this.cryptoCacheFile = new File(this.context.getCacheDir(), "httpCrypto.xml");
        }
        return cryptoCacheFile;
    }

    public String getPriKey() {
        return priKey;
    }

    public void setPriKey(String priKey) {
        this.priKey = priKey;
    }
}
