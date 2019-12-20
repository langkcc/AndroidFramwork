package com.juefeng.android.framework;

import com.juefeng.android.framework.down.DownHandler;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 16:14
 * Description:
 */
public interface LKDownFileManager {


    /**
     * down file.
     * support breakpoint resume
     * @param url url path
     * @param file  local file
     * @param headler  callback
     */
    void downFile(String url, File file, DownHandler headler);


    void downFile(String url, File file, int threadNumber,DownHandler headler);

}
