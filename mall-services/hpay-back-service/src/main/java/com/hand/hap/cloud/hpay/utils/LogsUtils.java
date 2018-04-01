package com.hand.hap.cloud.hpay.utils;

import com.hand.hap.cloud.hpay.entities.OutBoundLogs;
import com.hand.hap.cloud.hpay.entities.ThirdPartyApiLogs;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * author: zhangzilong
 * name: LogsUtils.java
 * discription: 用于存放日志的ThreadLocal
 * date: 2017/8/17
 * version: 0.1
 */

public class LogsUtils {

    private static Logger logger = LoggerFactory.getLogger(LogsUtils.class);

    private static ThreadLocal threadLocal = new ThreadLocal();

    public static OutBoundLogs getLogs() {
        return (OutBoundLogs) threadLocal.get();
    }

    public static void setLogs(OutBoundLogs t) {
        threadLocal.set(t);
    }

    public static void clear() {
        threadLocal.remove();
    }

    public static String saveMsg(String content, String type, String filePath) {
        File file = new File(filePath + type);
        try {
            FileUtils.writeStringToFile(file, content, "utf-8");
        } catch (IOException e) {
            logger.error(LogsUtils.class.getName(), e);
            return null;
        }
        return file.getPath();
    }
}
