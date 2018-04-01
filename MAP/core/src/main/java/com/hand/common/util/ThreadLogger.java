package com.hand.common.util;

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

    public void append(String logLine) {
        if (THREAD_LOG_LIST.get() == null) {
            clean();
        }
        THREAD_LOG_LIST.get().add(logLine);
    }

    public void clean() {
        THREAD_LOG_LIST.set(new LinkedList<>());
    }

    @Override
    public String toString() {
        if (THREAD_LOG_LIST.get() == null) {
            return "nothing";
        }
        StringBuilder logs = new StringBuilder("\n{:");
        for (String logLine : THREAD_LOG_LIST.get()) {
            logs.append("\n  -->> ").append(logLine);
        }
        return logs.append("\n:}\n").toString();

    }
}
