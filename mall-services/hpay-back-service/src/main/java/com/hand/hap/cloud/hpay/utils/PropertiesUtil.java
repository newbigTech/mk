package com.hand.hap.cloud.hpay.utils;

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
    private static final Properties HPAY_PROP = new Properties();
    private static final Properties PAY_INFO_PROP = new Properties();
    private static final String HPAY_PROPERTIES = "/hpayPropertiesFiles/hpay.properties";
    private static final String HPAYTEST_PROPERTIES = "/hpayPropertiesFiles/hpaytest.properties";
    private static final String PAY_INFO_PROPERTIES = "/hpayPropertiesFiles/payInfo.properties";
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    public static void init() {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            String testMode = System.getProperty("testmode");
            if ("true".equals(testMode)) {
                logger.info("加载测试环境配置文件");
                HPAY_PROP.load(PropertiesUtil.class.getResourceAsStream(HPAYTEST_PROPERTIES));
            } else {
                logger.info("加载正式环境配置文件");
                HPAY_PROP.load(PropertiesUtil.class.getResourceAsStream(HPAY_PROPERTIES));
            }
            PAY_INFO_PROP.load(PropertiesUtil.class.getResourceAsStream(PAY_INFO_PROPERTIES));
        } catch (final IOException e) {
            logger.info(e.getMessage());
        }
    }

    public static String getHpayValue(String key) {
        if (HPAY_PROP.isEmpty()) {
            loadProperties();
        }
        return HPAY_PROP.getProperty(key);
    }

    public static String getPayInfoValue(String key) {
        if (PAY_INFO_PROP.isEmpty()) {
            loadProperties();
        }
        return PAY_INFO_PROP.getProperty(key);
    }
}
