package com.juefeng.android.framework.down;

import com.juefeng.android.framework.LKDownFileManager;
import com.juefeng.android.framework.common.util.AppThreadPool;
import com.juefeng.android.framework.common.util.StringUtil;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 16:19
 * Description:
 */
public class LKDownFileManagerImpl implements LKDownFileManager {

    private static LKDownFileManager lkDownFileManager;

    private static Object lock = new Object();
    @Override
    public void downFile(String url, File file, int threadNumber,DownHandler headler) {
        if (StringUtil.isUrl(url)) {
            DownRequestor requestor = new DownRequestor(url,file,threadNumber,headler);
            new DownThread(requestor).start();
        }else{
            headler.onFailed();
        }

    }

    @Override
    public void downFile(String url, File file,DownHandler headler) {
        downFile(url,file,1,headler);
    }

    /**
     * get instance
     * @return
     */
    public static LKDownFileManager registerInstance() {
        if (lkDownFileManager==null){
            synchronized (lock){
                if (lkDownFileManager==null){
                    lkDownFileManager = new LKDownFileManagerImpl();
                }
            }
        }
        return lkDownFileManager;
    }
}
