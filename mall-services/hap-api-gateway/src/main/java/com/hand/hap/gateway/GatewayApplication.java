package com.hand.hap.gateway;

import com.hand.hap.gateway.filter.ProxyHeaderFilter;
import com.hand.hap.gateway.filter.SimpleCorsFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 唐磊
 * @Title:
 * @Description:
 * @date 2017/5/24 17:09
 */
@EnableZuulProxy
@RestController
@SpringBootApplication
@ComponentScan({"com.markor.unilog"})
@ImportResource(value = {"classpath:hsf/*.xml"})
public class GatewayApplication {
    private static Logger logger = LogManager.getLogger(GatewayApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public ProxyHeaderFilter proxyHeaderFilter() {
        return new ProxyHeaderFilter();
    }

    @Bean
    public SimpleCorsFilter simpleCorsFilter() {
        return new SimpleCorsFilter();
    }
}
