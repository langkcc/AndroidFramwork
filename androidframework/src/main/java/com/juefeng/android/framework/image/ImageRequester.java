package com.juefeng.android.framework.image;

import com.juefeng.android.framework.common.util.LogUtil;

import java.io.*;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/27
 * Time: 17:22
 * Description:
 */
public class ImageRequester implements Runnable{



    /**
     * image url
     */
    private String url;

    /**
     * file local path
     */
    private String path;

    private ImageReqesterHandler callback;

    public ImageRequester(String url, String path, ImageReqesterHandler callback) {
        this.path = path;
        this.url = url;
        this.callback = callback;
    }

    @Override
    public void run() {
        getImage();
    }

    /**
     * Do GET request
     *
     * @return
     * @throws Exception
     */
    public void getImage() {
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            URL localURL = new URL(url);
            URLConnection connection = this.openConnection(localURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            int count;
            byte[] bytes = new byte[1024];
            if (httpURLConnection.getResponseCode() >= 300) {
                if (connection!=null){
                    try {
                        connection.getInputStream().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                throw new Exception("HTTP Request is not success!!! Response code is " + httpURLConnection.getResponseCode());
            }
            inputStream = httpURLConnection.getInputStream();
            fos = new FileOutputStream(path);
            while ((count = inputStream.read(bytes)) > -1) {
                fos.write(bytes,0,count);
            }
            httpURLConnection.disconnect();
            if (connection!=null){
                try {
                    connection.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (callback!=null){
                callback.sendMessage(callback.obtainMessage(ImageHandler.SUCCESS,path));
            }
        }catch (Exception e){
            LogUtil.w(e);
            if (callback!=null){
                callback.sendMessage(callback.obtainMessage(ImageHandler.FAILED));
            }
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {

                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            }
        }
    }


    private URLConnection openConnection(URL localURL) throws IOException {
        URLConnection connection;
        connection = localURL.openConnection();
        return connection;
    }



}
