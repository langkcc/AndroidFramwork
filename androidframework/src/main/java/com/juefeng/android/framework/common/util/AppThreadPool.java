package com.juefeng.android.framework.common.util;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/12/15
 * Time: 11:40
 * Description:线程池工具类
 */
public class AppThreadPool {

    /**
     * 线程池大小
     */
    private static final int THREADS = 8;

    /**
     * 线程池
     */
    private static ExecutorService fixedThreadPool;

    /**
     * 线程池初始化
     */
    public static void init(){
        fixedThreadPool = new ThreadPoolExecutor(THREADS, THREADS,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }


    public static void execute(Runnable task){
        fixedThreadPool.execute(task);
    }


    /**
     * 线程池销毁
     */
    public static void shutdown(){
        if (fixedThreadPool!=null&&!fixedThreadPool.isShutdown()){
            fixedThreadPool.shutdown();
            fixedThreadPool = null;
        }
    }





}
