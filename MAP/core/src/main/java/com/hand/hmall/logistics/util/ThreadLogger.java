package com.hand.hmall.logistics.util;

import java.util.LinkedList;
import java.util.List;

/**
 * @author chenzhigang
 * @version 0.1
 * @name ThreadLogger
 * @description 记录线程日志的工具类
 * @date 2017/08/22
 */
public final class ThreadLogger {

    private static final ThreadLocal<List<String>> THREAD_LOG_LIST = new ThreadLocal();

    public ThreadLogger() {
        if (THREAD_LOG_LIST.get() == null) {
            THREAD_LOG_LIST.set(new LinkedList<>());
        }
    }

    public void append(String logLine) {
        THREAD_LOG_LIST.get().add(logLine);
    }

    @Override
    public String toString() {

        StringBuilder logs = new StringBuilder("{:");
        for (String logLine : THREAD_LOG_LIST.get()) {
            logs.append("\n  -->> ").append(logLine);
        }
        return logs.append("\n:}").toString();

    }
}
