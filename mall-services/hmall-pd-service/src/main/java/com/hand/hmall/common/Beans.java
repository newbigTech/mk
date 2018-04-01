package com.hand.hmall.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 容器启动时，从applicationContext中读取变量
 * 在CXF补丁代码中调用，用于补全URL地址
 *
 * @author alaowan
 *         Created at 2017/8/25 11:23
 * @description
 */
@Component
public class Beans implements ApplicationContextAware {

    // 微服务的serviceId
    private static String serviceId;

    public static String getApplicationServiceId() {
        return serviceId;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        serviceId = applicationContext.getEnvironment().getProperty("spring.application.name");
    }
}
