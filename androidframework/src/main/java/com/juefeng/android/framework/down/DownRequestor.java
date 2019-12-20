package com.juefeng.android.framework.down;

import android.os.Build;
import com.juefeng.android.framework.LKUtil;
import com.juefeng.android.framework.common.util.IOUtil;
import com.juefeng.android.framework.common.util.LogUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/27
 * Time: 17:22
 * Description:download file requestor and logic handle
 */
public class DownRequestor {

    private static File downCacheKeyFile = LKUtil.getAppConfig().getExternalDownCacheDir();

    /**
     * file local
     */
    private File file;

    /**
     * down file hander callback
     */
    private DownHandler callback;

    /**
     * file url path
     */
    private String filepath = null;

    /**
     * thread number
     */
    private int threadNum = 1;

    /**
     * set count latch
     */
    private CountDownLatch latch = null;

    private long fileLength = 0L;

    private long threadLength = 0L;

    /**
     * set threads start position
     */
    private long[] startPos;
    /**
     * set threads end position
     */
    private long[] endPos;

    /**
     * whether canceled
     */
    private boolean bool = false;

    private URL url = null;


    public DownRequestor(String filepath, File file, int threadNum, DownHandler callback) {
        this.file = file;
        this.filepath = filepath;
        this.threadNum = threadNum;
        this.callback = callback;
        startPos = new long[this.threadNum];
        endPos = new long[this.threadNum];
        latch = new CountDownLatch(this.threadNum);
    }

    /**
     * start download file
     */
    public void download() {

        //temp file array
        File[] tmpfile = new File[threadNum];
        HttpURLConnection httpcon = null;

        //get file name
        String filename = filepath.substring(filepath.lastIndexOf('/') + 1, filepath
                .contains("?") ? filepath.lastIndexOf('?') : filepath.length());
        //generation file temp file name
        String tmpfilename = filename + "_tmp";

        try {
            url = new URL(filepath);
            httpcon = (HttpURLConnection) url.openConnection();

            setHeader(httpcon);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fileLength = httpcon.getContentLengthLong();
            } else {
                fileLength = httpcon.getContentLength();
            }

            for (int i = 0; i < threadNum; i++) {
                tmpfile[i] = new File(downCacheKeyFile, tmpfilename + "_" + (i + 1));
            }

            //every thread length
            threadLength = fileLength / threadNum;
            LogUtil.d("fileName: " + filename + " ," + "fileLength= "
                    + fileLength + " the threadLength= " + threadLength);

            if (file.exists() && file.length() == fileLength) {
                LogUtil.d("the file you want to download has exited!!");
                if (callback != null) {
                    callback.onSuccess(file.getAbsolutePath());
                }
                return;
            } else {
                setBreakPoint(startPos, endPos, tmpfile);
                //thread pool
                ExecutorService exec = Executors.newCachedThreadPool();
                for (int i = 0; i < threadNum; i++) {
                    exec.execute(new DownLoadThread(startPos[i], endPos[i],
                            this, i, tmpfile[i], latch));
                }
                //wait countlatch equals zero
                latch.await();
                //shutdown
                exec.shutdown();
            }
            LogUtil.d("temp file is downloaded.unify file");
            unifyFile(tmpfile);
            LogUtil.d("complete unify file");
            if (callback != null) {
                callback.onSuccess(file.getAbsolutePath());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailed();
            }
        } catch (IOException e) {
            LogUtil.w(e);
            if (callback != null) {
                callback.onFailed();
            }
        } catch (InterruptedException e) {
            LogUtil.w(e);
            if (callback != null) {
                callback.onFailed();
            }
        } finally {
            if (file.length() == fileLength) {
                for (int i = 0; i < threadNum; i++) {
                    if (tmpfile[i].exists()) {
                        LogUtil.d("delect the temp file!!");
                        tmpfile[i].delete();
                    }
                }

            }
        }
    }

    /**
     * unify file from temp file
     */
    private void unifyFile(File[] temFiles) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            for (int i = 0; i < threadNum; i++) {
                FileInputStream item = new FileInputStream(temFiles[i]);
                IOUtil.copy(item, fos);
                item.close();
            }
            fos.close();
        } catch (IOException e) {
            LogUtil.w(e);
        }

    }

    /**
     * set break point,compute every thread download content limit
     * @param startPos
     * @param endPos
     * @param tmpfile
     */
    private void setBreakPoint(long[] startPos, long[] endPos, File[] tmpfile) {
        try {
            for (int i = 0; i < threadNum; i++) {
                if (i == threadNum - 1) {
                    endPos[i] = fileLength;
                } else {
                    endPos[i] = threadLength * (i + 1) - 1;
                }
                if (tmpfile[i].exists() && tmpfile[i].length() > endPos[i]) {
                    tmpfile[i].delete();
                }
                if (tmpfile[i].exists()) {
                    startPos[i] = threadLength * i + tmpfile[i].length();
                } else {
                    startPos[i] = threadLength * i;
                }
                LogUtil.d("the Array content in the exit file: ");
                LogUtil.d("thre thread" + (i + 1) + " startPos:"
                        + startPos[i] + ", endPos: " + endPos[i]);
            }
        } catch (Exception e) {
            LogUtil.w(e);
        }
    }

    /**
     * sub thread,download limit content
     */
    class DownLoadThread implements Runnable {

        /**
         * start position
         */
        private long startPos;
        /**
         * end position
         */
        private long endPos;
        /**
         * parent task
         */
        private DownRequestor task = null;
        /**
         * sub id
         */
        private int id;
        /**
         * temp file
         */
        private File tmpfile = null;
        /**
         * file operation tool
         */
        private RandomAccessFile rantmpfile = null;
        /**
         * task's count latch
         */
        private CountDownLatch latch = null;

        public DownLoadThread(long startPos, long endPos,
                              DownRequestor task, int id, File tmpfile,
                              CountDownLatch latch) {
            this.startPos = startPos;
            this.endPos = endPos;
            this.task = task;
            this.tmpfile = tmpfile;
            try {
                this.rantmpfile = new RandomAccessFile(this.tmpfile, "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            this.id = id;
            this.latch = latch;
        }

        @Override
        public void run() {
            HttpURLConnection httpcon;
            InputStream is = null;
            int length;
            System.out.println("the thread " + id + " has started!!");
            try {
                URLConnection connection =  task.url.openConnection();
                httpcon = (HttpURLConnection) connection;
                setHeader(httpcon);

                //judge start and end position
                if (startPos < endPos) {
                    //set header get limit start position by end position
                    httpcon.setRequestProperty("Range", "bytes=" + startPos
                            + "-" + endPos);
                    LogUtil.d("Thread " + id
                            + " the total size:---- "
                            + (endPos - startPos));
                    if (this.tmpfile.exists()) {
                        rantmpfile.seek(this.tmpfile.length());
                    }
                    if (httpcon.getResponseCode() != HttpURLConnection.HTTP_OK
                            && httpcon.getResponseCode() != HttpURLConnection.HTTP_PARTIAL) {
                        this.task.bool = true;
                        LogUtil.d("server return responce code is" + httpcon.getResponseCode());
                        connection.getInputStream().close();
                        httpcon.disconnect();
                        rantmpfile.close();
                        LogUtil.d("the thread ---" + id + " has done!!");
                        latch.countDown();
                        return;
                    }

                    is = httpcon.getInputStream();
                    long count = 0L;
                    byte[] buf = new byte[1024];
                    while (!this.task.bool && (length = is.read(buf)) != -1) {
                        count += length;
                        rantmpfile.write(buf, 0, length);
                    }
                    LogUtil.d("the thread " + id + " total load count: " + count);
                    connection.getInputStream().close();
                    httpcon.disconnect();
                    rantmpfile.close();
                }
                //count auto down
                latch.countDown();
                LogUtil.d("the thread " + id + " has done!!");
                return;
            } catch (IOException e) {
                LogUtil.w(e);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    LogUtil.w(e);
                }
            }
        }
    }

    /*
     * set http header
     */
    private void setHeader(HttpURLConnection con) {
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 Build/LYZ28E) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Mobile Safari/537.36");
        con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        con.setRequestProperty("Accept-Encoding", "identity");
        con.setRequestProperty("Accept-Charset",
                "zh-CN,zh;q=0.8");
        con.setRequestProperty("Connection", "keep-alive");
    }

}
