package com.hand.hap.cloud.thirdParty.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author jianjun.tan
 * @Title PropertiesUtil
 * @date 2017/05/25 19:08
 */
public abstract class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static final Properties QQ_PROP = new Properties();

    private static final Properties WECHAT_PROP = new Properties();

    private static final Properties ZMALL_PROP = new Properties();

    static {

        loadProperties();
    }

    private static final String QQ_PROPERTIES = "/qqconnectconfig.properties";

    private static final String WECHAT_PROPERTIES = "/wechatconnectconfig.properties";

    private static final String ZMALL_PROPERTIES = "/zmallConfig.properties";


    private static void loadProperties() {
        try {
            QQ_PROP.load(PropertiesUtil.class.getResourceAsStream(QQ_PROPERTIES));
            WECHAT_PROP.load(PropertiesUtil.class.getResourceAsStream(WECHAT_PROPERTIES));
            ZMALL_PROP.load(PropertiesUtil.class.getResourceAsStream(ZMALL_PROPERTIES));
        } catch (final IOException e) {
            logger.info(e.getMessage());
        }
    }

    public static String getQQValue(String key) {
        if (QQ_PROP.isEmpty()) {
            loadProperties();
        }
        return QQ_PROP.getProperty(key);
    }

    public static String getWechatValue(String key) {
        if (WECHAT_PROP.isEmpty()) {
            loadProperties();
        }
        return WECHAT_PROP.getProperty(key);
    }

    public static String getZmallValue(String key) {
        if (ZMALL_PROP.isEmpty()) {
            loadProperties();
        }
        return ZMALL_PROP.getProperty(key);
    }
}
