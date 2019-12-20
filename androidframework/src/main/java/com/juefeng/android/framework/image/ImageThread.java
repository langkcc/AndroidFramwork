package com.juefeng.android.framework.image;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 16:24
 * Description:
 */
public class ImageThread extends Thread {


    /**
     * Image Requestor
     */
    private ImageRequester imageRequester;

    public ImageThread(ImageRequester requestor){
        this.imageRequester = requestor;
    }

    @Override
    public void run() {
        imageRequester.getImage();
    }
}
