package com.hand.promotion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/2/7
 * @description
 */
@Configuration
@ImportResource(value = "classpath:hsf/*.xml")
@Import({CacheConfig.class, RocketmqAutoConfiguration.class})
public class SystemConfig {


}
