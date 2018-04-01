package com.hand.hmall.dto;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author baihua
 * @version 0.1
 * @name ThreadFactory$
 * @description $END$
 * @date 2017/8/19$
 */
public class ThreadFactory {

    static ExecutorService threadPool = Executors.newFixedThreadPool(2000);

    public static ExecutorService getThreadPool() {
        return threadPool;
    }

    public static void setThreadPool(ExecutorService threadPool) {
        ThreadFactory.threadPool = threadPool;
    }
}
