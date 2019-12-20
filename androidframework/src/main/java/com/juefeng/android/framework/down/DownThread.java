package com.juefeng.android.framework.down;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 16:25
 * Description:
 */
public class DownThread extends Thread{


    private DownRequestor downRequestor;

    public DownThread(DownRequestor requestor){
        this.downRequestor = requestor;
    }

    @Override
    public void run() {
        downRequestor.download();
    }
}
