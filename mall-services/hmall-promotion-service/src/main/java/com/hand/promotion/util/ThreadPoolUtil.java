package com.hand.promotion.util;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author XinyangMei
 * @Title ThreadPoolUtil
 * @Description desp
 * @date 2017/12/11 16:34
 */
public class ThreadPoolUtil {

    private static ExecutorService threadPoolExecutor;

    private static ExecutorService getThreadPool() {
        if (threadPoolExecutor == null) {
//            threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.valueOf(unit),
//                    new ArrayBlockingQueue<Runnable>(5));
            threadPoolExecutor = Executors.newFixedThreadPool(500);
        }
        return threadPoolExecutor;
    }

    public static void submit(Runnable task) {
        getThreadPool().submit(task);
    }

    public static Future submit(Callable task) {
        return getThreadPool().submit(task);
    }

}
